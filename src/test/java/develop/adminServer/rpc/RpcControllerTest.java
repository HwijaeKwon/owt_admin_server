package develop.adminServer.rpc;

import com.google.gson.JsonArray;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootTest
class RpcControllerTest {

    @Autowired
    private RpcController rpcController;

    @Test
    public void basicRpcTest() {

        JsonArray args = new JsonArray();
        Pair<Flux<Pair<String, String>>, Integer> replay = rpcController.sendMessage("test-receiver", "test", new JSONArray(), null);
        Flux<Pair<String, String>> stream = replay.getLeft();
        Integer corrID = replay.getRight();

        System.out.println("corrID = " + corrID);

        Pair<String, String> result = stream.blockFirst(Duration.ofSeconds(3));

        System.out.println("result.getLeft() = " + result.getLeft());
    }
}