package com.effective.mobile.tskmngmntsystm.controller.impl;

import com.effective.mobile.tskmngmntsystm.controller.TaskApi;
import com.effective.mobile.tskmngmntsystm.dto.TaskRq;
import com.effective.mobile.tskmngmntsystm.dto.TaskRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import com.effective.mobile.tskmngmntsystm.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @Override
    public TaskServiceResponse<TaskRs> createTask(TaskRq taskRq) {
        log.info("Получен запрос на создание новой задачи");
        return taskService.createTask(taskRq);
    }

    @Override
    public TaskServiceResponse<TaskRs> updateTask(TaskRq taskRq, Long id) {
        log.info("Получен запрос на изменение задачи с id = {}", id);
        return taskService.updateTask(taskRq, id);
    }

    @Override
    public TaskServiceResponse<TaskRs> updateTaskStatus(Status newStatus, Long id) {
        log.info("Получен запрос на изменение статуса задачи с id = {}", id);
        return taskService.updateTaskStatus(newStatus, id);
    }

    @Override
    public TaskServiceResponse<TaskRs> deleteTask(Long id) {
        log.info("Получен запрос на удаление задачи с id = {}", id);
        return taskService.deleteTask(id);
    }

    @Override
    public TaskServiceResponse<TaskRs> getTaskById(Long id) {
        log.info("Получен запрос на получение задачи с id = {}", id);
        return taskService.getTaskById(id);
    }

    @Override
    public TaskServiceResponse<List<TaskRs>> getAllTasks(Long authorId, Long performerId, int page, int size) {
        log.info("Получен запрос на получение задачи с authorId = {}, и с performerId id = {}", authorId, performerId);
        return taskService.getAllTasks(authorId, performerId, page, size);
    }
}
