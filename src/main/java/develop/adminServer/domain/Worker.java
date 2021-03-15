package develop.adminServer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Worker {

    private String id;

    private String purpose;

    private String ip;

    private String rpcID;

    private Number state;

    private Number load;

    private String hostname;

    private Number port;

    private Number keepAlive;
}
