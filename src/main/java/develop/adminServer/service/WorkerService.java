package develop.adminServer.service;

import develop.adminServer.dto.ListDto;
import develop.adminServer.dto.WorkerInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerService {

    public ListDto<List<WorkerInfo>> findAll(String purpose) {
        WorkerInfo workerInfo = new WorkerInfo("", 1, 1, 1, "");
        List<WorkerInfo> list = new ArrayList<WorkerInfo>();
        list.add(workerInfo);
        return new ListDto<List<WorkerInfo>>(3, list);
    }
}
