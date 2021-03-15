package develop.adminServer.service;

import develop.adminServer.common.RpcReplyErrorException;
import develop.adminServer.domain.Worker;
import develop.adminServer.rpc.RpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final RpcService rpcService;

    /**
     * 특정 Purpose에 대한 worker들을 조회한다
     * @param purpose purpose
     * @param count 조회할 최대 worker의 수
     * @return Worker List
     * @throws RuntimeException rpc runtime error
     * @throws RpcReplyErrorException rpc error
     */
    public List<Worker> findWorkerByPurpose(String purpose, int count) throws RuntimeException, RpcReplyErrorException {
        List<String> workerIds = rpcService.getWorkers(purpose, 3000L, count);
        List<Worker> workers = new ArrayList<>();
        for (String id : workerIds) {
            Worker workerAttr = rpcService.getWorkerAttr(id, 3000L);
            workers.add(workerAttr);
        }
        return workers;
    }

    /**
     * 특정 worker에 대한 정보를 반환한다
     * @param id worker id
     * @return Worker
     * @throws RuntimeException rpc runtime error
     * @throws RpcReplyErrorException rpc error
     */
    public Worker findWorkerById(String id) throws RuntimeException, RpcReplyErrorException {
        return rpcService.getWorkerAttr(id, 3000L);
    }

    /**
     * 특정 purpose와 task에 스케줄링된 worker를 반환한다
     * @param purpose worker purpose
     * @param task worker task
     * @return Worker Id
     * @throws RuntimeException rpc runtime error
     * @throws RpcReplyErrorException rpc error
     */
    public Worker findWorkerByPurposeAndTask(String purpose, String task) throws RuntimeException, RpcReplyErrorException {
        String workerId = rpcService.getScheduled(purpose, task, 3000L);
        return rpcService.getWorkerAttr(workerId, 3000L);
    }
}
