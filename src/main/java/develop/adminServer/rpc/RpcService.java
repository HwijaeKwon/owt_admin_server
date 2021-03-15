package develop.adminServer.rpc;

import com.google.gson.Gson;
import develop.adminServer.common.RpcReplyErrorException;
import develop.adminServer.domain.Worker;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RpcService {

    private final RpcController rpcController;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private String clusterName = "owt-cluster";

    private Gson gson = new Gson();

    /**
     * Purpose에 해당하는 모든 worker를 조회한다
     * @param purpose worker purpose
     * @param rpcTimeout Rpc 응답을 기다리는 최대 시간 (ms)
     * @param count 조회할 maximum worker 수
     * @return worker id 리스트
     * @throws RuntimeException rpc runtime error
     * @throws RpcReplyErrorException rpc error
     */
    public List<String> getWorkers(String purpose, Long rpcTimeout, int count) throws RuntimeException, RpcReplyErrorException {
        JSONArray args = new JSONArray();
        args.put(purpose);
        RpcResult result = rpcController.sendMessage(clusterName, "getWorkers", args, null);
        Flux<RpcReply> stream = result.getStream();
        Integer corrID = result.getCorrID();

        RpcReply reply = stream.blockFirst(Duration.ofMillis(rpcTimeout));
        rpcController.deleteCorrID(corrID);

        if (reply == null || reply.getData().equals("error")) {
            String error = (reply != null && reply.getError() != null)? reply.getError() :  "Rpc reply error";
            throw new RpcReplyErrorException(error);
        }

        List<String> workerList = new ArrayList<String>();

        try {
            JSONArray jsonResult = new JSONArray(reply.getData());
            int i  = 0;
            while(i < count) {
                workerList.add(jsonResult.getString(i));
                i++;
            }
        } catch (JSONException exception) {
            //
        }

        return workerList;
    }

    /**
     * Worker 정보를 조회한다
     * @param id worker id
     * @param rpcTimeout Rpc 응답을 기다리는 최대 시간 (ms)
     * @return WorkerAttr
     * @throws RuntimeException rpc runtime error
     * @throws RpcReplyErrorException rpc error
     */
    public Worker getWorkerAttr(String id, Long rpcTimeout) throws RuntimeException, RpcReplyErrorException {
        JSONArray args = new JSONArray();
        args.put(id);
        RpcResult result = rpcController.sendMessage(clusterName, "getWorkerAttr", args, null);
        Flux<RpcReply> stream = result.getStream();
        Integer corrID = result.getCorrID();

        RpcReply reply = stream.blockFirst(Duration.ofMillis(rpcTimeout));
        rpcController.deleteCorrID(corrID);

        if (reply == null || reply.getData().equals("error")) throw new RpcReplyErrorException(reply != null ? reply.getError() : "Rpc reply error");

        return gson.fromJson(reply.getData(), Worker.class);
    }

    /**
     * Purpose와 task에 맞는 Worker를 반환한다
     * @param purpose worker purpose
     * @param task worker task
     * @param rpcTimeout Rpc 응답을 기다리는 최대 시간 (ms)
     * @return WorkerId
     * @throws RuntimeException rpc runtime error
     * @throws RpcReplyErrorException rpc error
     */
    public String getScheduled(String purpose, String task, Long rpcTimeout) throws RuntimeException, RpcReplyErrorException {
        JSONArray args = new JSONArray();
        args.put(purpose);
        args.put(task);
        RpcResult result = rpcController.sendMessage(clusterName, "getScheduled", args, null);
        Flux<RpcReply> stream = result.getStream();
        Integer corrID = result.getCorrID();

        RpcReply reply = stream.blockFirst(Duration.ofMillis(rpcTimeout));
        rpcController.deleteCorrID(corrID);

        if (reply == null || reply.getData().equals("error")) throw new RpcReplyErrorException(reply != null ? reply.getError() : "Rpc reply error");

        return reply.getData();
    }
}
