package develop.adminServer.controller;

import develop.adminServer.dto.ListDto;
import develop.adminServer.dto.WorkerInfo;
import develop.adminServer.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/api/workers/{purpose}")
    @Operation(summary = "Find all workers", description = "Find all workers according to purpose")
    @ApiResponse(description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(description = "Worker info list", nullable = false, implementation = ListDto.class)))
    public ListDto findAll(
            @Parameter(description = "The purpose of workers want to find", required = true)
            @PathVariable("purpose") String purpose) {
        return workerService.findAll(purpose);
    }
}