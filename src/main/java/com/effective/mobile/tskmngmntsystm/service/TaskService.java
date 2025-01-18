package com.effective.mobile.tskmngmntsystm.service;


import com.effective.mobile.tskmngmntsystm.dto.TaskRq;
import com.effective.mobile.tskmngmntsystm.dto.TaskRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    TaskServiceResponse<TaskRs> createTask(TaskRq taskRq);

    TaskServiceResponse<TaskRs> updateTask(TaskRq taskRq, Long id);

    TaskServiceResponse<TaskRs> updateTaskStatus(Status newStatus, Long id);

    TaskServiceResponse<TaskRs> deleteTask(Long id);

    TaskServiceResponse<TaskRs> getTaskById(Long id);

    TaskServiceResponse<List<TaskRs>> getAllTasks(Long authorId, Long performerId, int page, int size);

}
