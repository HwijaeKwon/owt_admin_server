package develop.adminServer.rpc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Flux;

@Getter
@AllArgsConstructor
public class RpcResult {

    private Flux<RpcReply> stream;

    private Integer corrID;
}
