package develop.adminServer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ListDto<T> {

    @Schema(description = "Number of data in the list", minimum = "0")
    @NotNull
    private Number count;

    @Schema(description = "Data list")
    @NotNull
    private T data;
}
