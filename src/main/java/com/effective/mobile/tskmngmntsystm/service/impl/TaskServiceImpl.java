package com.effective.mobile.tskmngmntsystm.service.impl;

import com.effective.mobile.tskmngmntsystm.dto.TaskRq;
import com.effective.mobile.tskmngmntsystm.dto.TaskRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import com.effective.mobile.tskmngmntsystm.exception.CustomException;
import com.effective.mobile.tskmngmntsystm.mapper.TaskMapper;
import com.effective.mobile.tskmngmntsystm.models.TaskEntity;
import com.effective.mobile.tskmngmntsystm.models.TaskSpecification;
import com.effective.mobile.tskmngmntsystm.repository.TaskRepository;
import com.effective.mobile.tskmngmntsystm.service.TaskService;
import com.effective.mobile.tskmngmntsystm.util.CurrentUserUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public TaskServiceResponse<TaskRs> createTask(TaskRq taskRq) {
        try {
            Long currentUserId = currentUserUtil.getCurrentUserId();
            TaskEntity taskEntity = taskMapper.toTaskEntity(taskRq);
            taskEntity.setAuthorId(currentUserId);
            taskEntity = taskRepository.save(taskEntity);
            log.info("Задача создана");
            return TaskServiceResponse.ok("Задача создана", taskMapper.toTaskRs(taskEntity));
        } catch (Exception e) {
            log.error("Не удалось создать задачу. Error: {}", e.getMessage());
            throw new CustomException("Не удалось создать задачу, возможно такая задача уже существует", e);
        }
    }

    @Override
    public TaskServiceResponse<TaskRs> updateTask(TaskRq taskRq, Long id) {
        try {
            TaskEntity taskEntity = taskMapper.toTaskEntity(taskRq, id);
            taskEntity.setAuthorId(currentUserUtil.getCurrentUserId()); // додумать этот момент
            taskEntity = taskRepository.save(taskEntity);
            log.info("Задача сохранена. id = " + taskEntity.getId());
            return TaskServiceResponse.ok("Задача сохранена", taskMapper.toTaskRs(taskEntity));
        } catch (Exception e) {
            log.error("Не удалось обновить задачу. Error: {}", e.getMessage());
            throw new CustomException("Ошибка при обновлении задачи ", e);
        }
    }

    @Override
    @Transactional
    public TaskServiceResponse<TaskRs> updateTaskStatus(Status newStatus, Long id) {
        if (!currentUserUtil.isMatching(id)) {
            log.error("Пользователь не является исполнителем данной задачи");
            return TaskServiceResponse.notOk("Вы не являетесь исполнителем данной задачи", HttpStatus.BAD_REQUEST);
        }
        try {
            int updatedRows = taskRepository.updateTaskStatus(newStatus, id);
            if (updatedRows == 0) {
                throw new EntityNotFoundException("Задача с id " + id + " не найдена");
            }
            TaskEntity updatedTask = taskRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Задача с id " + id + " не найдена"));
            log.info("Статус изменен. id = " + updatedTask.getId());
            return TaskServiceResponse.ok("Статус изменен", taskMapper.toTaskRs(updatedTask));
        } catch (Exception e) {
            log.error("Не удалось обновить задачу. Error: {}", e.getMessage());
            throw new CustomException("Ошибка при обновлении задачи ", e);
        }
    }


    @Override
    public TaskServiceResponse<TaskRs> deleteTask(Long id) {
        try {
            if (!taskRepository.existsById(id)) {
                return TaskServiceResponse.notOk("Задача, с id = " + id + ", удалена", HttpStatus.NOT_FOUND);
            }
            taskRepository.deleteById(id);
            log.info("Задача, с id = " + id + ", удалена");
            return TaskServiceResponse.ok("Задача, с id = " + id + ", удалена");
        } catch (Exception e) {
            log.error("Ошибка при удалении задачи с id :{}. Error: {}", id, e.getMessage());
            throw new CustomException("Ошибка при удалении задачи с id = " + id, e);
        }
    }

    @Override
    public TaskServiceResponse<TaskRs> getTaskById(Long id) {
        TaskEntity resultTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Задача с id " + id + " не найдена"));
        log.info("Задача с id = " + id + " найдена");
        return TaskServiceResponse.ok("Задача с id = " + id + " найдена", taskMapper.toTaskRs(resultTask));
    }

    @Override
    public TaskServiceResponse<List<TaskRs>> getAllTasks(Long authorId, Long performerId, int page, int size) {
        Specification<TaskEntity> filters = Specification.where(authorId == null || authorId <= 0 ? null : TaskSpecification.authorIdIs(authorId))
                .and(performerId == null || performerId <= 0 ? null : TaskSpecification.performerIdIs(performerId));
        try {
            Pageable pageable = PageRequest.of(page, size);
            org.springframework.data.domain.Page<TaskEntity> taskPage = taskRepository.findAll(filters, pageable);
            List<TaskRs> tasks = taskPage.getContent()
                    .stream()
                    .map(taskMapper::toTaskRs)
                    .toList();
            log.info("Успешно получен список всех задач");
            return TaskServiceResponse.ok("Успешно получен список всех задач", tasks);
        } catch (Exception e) {
            log.error("Ошибка при получении списка задач. Error: {}", e.getMessage());
            throw new CustomException("Ошибка при получпении списка задач", e);
        }
    }
}
