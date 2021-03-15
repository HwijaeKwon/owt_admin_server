package develop.adminServer.rpc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RpcReply {
    private String data;
    private String error;
}
