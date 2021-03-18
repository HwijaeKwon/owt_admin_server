package develop.adminServer.controller;

import develop.adminServer.common.RpcReplyErrorException;
import develop.adminServer.domain.Worker;
import develop.adminServer.dto.WorkerDto;
import develop.adminServer.dto.WorkerListDto;
import develop.adminServer.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @GetMapping("/api/workers")
    @Operation(summary = "Find worker by purpose", description = "Get workers according to purpose")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(description = "Worker list", implementation = WorkerListDto.class))),
            @ApiResponse(description = "Rpc Timeout or Rpc Error", responseCode = "500", content = @Content(mediaType = "text_plain", schema = @Schema(description = "Error message", example = "Timeout on blocking for ~ seocnds", implementation = String.class))),
    })
    public WorkerListDto findWorkerByPurpose(
            @Parameter(description = "Purpose of the worker")
            @RequestParam(name = "purpose", required = false, defaultValue = "all") String purpose,
            @Parameter(description = "Maximum number of worker to get")
            @RequestParam(name = "count", required = false, defaultValue = "10") int count) throws RuntimeException, RpcReplyErrorException {

            List<Worker> workers = workerService.findWorkerByPurpose(purpose, count);
            return new WorkerListDto(workers.size(), workers.stream().map(WorkerDto::new).collect(Collectors.toList()));
    }

    @GetMapping("/api/workers/{workerId}")
    @Operation(summary = "Find worker by id", description = "Get worker according to worker id")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(description = "Worker attribute", implementation = WorkerDto.class))),
            @ApiResponse(description = "Rpc Timeout or Rpc Error", responseCode = "500", content = @Content(mediaType = "text_plain", schema = @Schema(description = "Error message", example = "Timeout on blocking for ~ seocnds", implementation = String.class))),
    })
    public WorkerDto findWorkerById(
            @Parameter(description = "Unique identifier of the worker to get", required = true)
            @PathVariable("workerId") String id) throws RuntimeException, RpcReplyErrorException {

        return new WorkerDto(workerService.findWorkerById(id));
    }

    @GetMapping("/api/workers/scheduled")
    @Operation(summary = "Find scheduled worker by purpose and task", description = "Get scheduled worker according to purpose and task")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(description = "Worker attribute", implementation = WorkerDto.class))),
            @ApiResponse(description = "Rpc Timeout or Rpc Error", responseCode = "500", content = @Content(mediaType = "text_plain", schema = @Schema(description = "Error message", example = "Timeout on blocking for ~ seocnds", implementation = String.class))),
    })
    public WorkerDto findScheduledWorker(
            @Parameter(description = "Purpose of the worker to get", required = true)
            @RequestParam(name = "purpose") String purpose,
            @Parameter(description = "Task of the worker to get", required = true)
            @RequestParam(name = "task") String task) throws RuntimeException, RpcReplyErrorException {

        return new WorkerDto(workerService.findWorkerByPurposeAndTask(purpose, task));
    }
}