package develop.adminServer.rpc;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class RpcController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final HashMap<Integer, Sinks.Many<Pair<String, String>>> processorMap = new HashMap<Integer, Sinks.Many<Pair<String, String>>>();

    private final Sinks.Many<Message<String>> sendProcessor = Sinks.many().unicast().onBackpressureBuffer();

    public Pair<Flux<Pair<String, String>>, Integer> sendMessage(String routingKey, String method, JSONArray args, Integer corrID) {

        if(corrID == null) {
            int maxCorrID = 10000;
            corrID = new Random().nextInt(maxCorrID);
            if(processorMap.containsKey(corrID)) throw new IllegalStateException("Generate Corr ID fail");
            processorMap.put(corrID, Sinks.many().unicast().onBackpressureBuffer());
        }

        JSONObject message = new JSONObject();

        try {
            message.put("method", method);
            message.put("args", args.toString());
            message.put("corrID", corrID);
            String bindingRoutingKey = "admin";
            message.put("replyTo", bindingRoutingKey);
        } catch (JSONException exception) {
            processorMap.remove(corrID);
            throw new IllegalStateException("Create message fail");
        }

        Message<String> msg = MessageBuilder.withPayload(message.toString())
                .setHeader("routingKey", routingKey)
                .build();

        Integer validCorrID = corrID;
        sendProcessor.emitNext(msg, (signalType, emitResult) -> {
            if(emitResult == Sinks.EmitResult.FAIL_NON_SERIALIZED) {
                LockSupport.parkNanos(10);
                return true;
            } else {
                processorMap.remove(validCorrID);
                return false;
            }
        });

        return Pair.of(processorMap.get(corrID).asFlux(), corrID);
    }

    private void receiveMessage(String message) throws JSONException {
        System.out.println("rx!!: " + message);
        JSONObject jsonMessage = new JSONObject(message);
        Integer corrID = jsonMessage.optInt("corrID");
        String data = jsonMessage.optString("data");
        String error = "";
        if(jsonMessage.has("err")) error = jsonMessage.optString("err");
        if(!processorMap.containsKey(corrID))
            throw new IllegalStateException("Receiver processor not found: " + corrID);
        System.out.println("ok!!!");
        processorMap.get(corrID).emitNext(Pair.of(data, error), Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public void deleteCorrID(Integer corrID) {
        processorMap.remove(corrID);
    }

    @Bean
    public Supplier<Flux<Message<String>>> tx() {
        return sendProcessor::asFlux;
    }

    @Bean
    public Function<Flux<Message<String>>, Mono<Void>> rx() {
        return stream -> stream.doOnNext(
                msg -> {
                    try {
                        receiveMessage(msg.getPayload());
                    } catch (JSONException exception) {
                        throw new IllegalStateException("Receive message fail");
                    }
                })
                .onErrorContinue((throwable, o)
                        -> logger.error("Rpc rx error at {}, with {}", o, throwable.getLocalizedMessage()))
                .then();
    }
}
