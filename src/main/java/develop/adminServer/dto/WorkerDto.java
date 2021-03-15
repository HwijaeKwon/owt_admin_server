package develop.adminServer.dto;

import develop.adminServer.domain.Worker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class WorkerDto {

    public WorkerDto(Worker worker) {
        this.id = worker.getId();
        this.purpose = worker.getPurpose();
        this.ip = worker.getIp();
        this.rpcID = worker.getRpcID();
        this.state = worker.getState();
        this.load = worker.getLoad();
        this.hostname = worker.getHostname();
        this.port = worker.getPort();
        this.keepAlive = worker.getKeepAlive();
    }

    @Schema(description = "Unique identifier of the worker.", minLength = 1)
    @NotNull @NotBlank
    private String id;

    @Schema(description = "Purpose of the worker.", minLength = 1, example = "video")
    @NotNull @NotBlank
    private String purpose;

    @Schema(description = "Ip address of the worker.", example = "127.0.0.1")
    @NotNull
    private String ip;

    @Schema(description = "Unique rpc identifier of the worker.", minLength = 1)
    @NotNull @NotBlank
    private String rpcID;

    @Schema(description = "State of the worker. [0 | 1 | 2]", minimum = "0", maximum = "2", example = "1")
    @NotNull
    private Number state;

    @Schema(description = "CPU/GPU load under which this worker can take new task.", minimum = "0", maximum = "1", example = "0.85")
    @NotNull
    private Number load;

    @Schema(description = "Hostname of the worker.", minLength = 1)
    @NotNull @NotBlank
    private String hostname;

    @Schema(description = "Port of the worker.", minimum = "1")
    @NotNull
    private Number port;

    @Schema(description = "Alive count of the worker.", example = "1")
    @NotNull
    private Number keepAlive;
}
