package develop.adminServer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class WorkerInfo {

    @Schema(description = "Purpose of the worker.", minLength = 1, example = "video")
    @NotNull @NotBlank
    private String purpose;

    @Schema(description = "State of the worker. [0 | 1 | 2]", minimum = "0", maximum = "2", example = "1")
    @NotNull
    private Number state;

    @Schema(description = "Maximum CPU/GPU load under which this worker can take new task.", minimum = "0", maximum = "1", example = "0.85")
    @NotNull
    private Number maxLoad;

    @Schema(description = "CPU/GPU load under which this worker can take new task.", minimum = "0", maximum = "1", example = "0.85")
    @NotNull
    private Number load;

    @Schema(description = "Ip address of the worker.", example = "127.0.0.1")
    @NotNull
    private String ip;
}
