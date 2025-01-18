package com.effective.mobile.tskmngmntsystm.service.impl;

import com.effective.mobile.tskmngmntsystm.dto.CommentRq;
import com.effective.mobile.tskmngmntsystm.dto.CommentRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.enums.Role;
import com.effective.mobile.tskmngmntsystm.exception.CustomException;
import com.effective.mobile.tskmngmntsystm.mapper.CommentMapper;
import com.effective.mobile.tskmngmntsystm.models.CommentEntity;
import com.effective.mobile.tskmngmntsystm.models.TaskEntity;
import com.effective.mobile.tskmngmntsystm.repository.CommentRepository;
import com.effective.mobile.tskmngmntsystm.repository.TaskRepository;
import com.effective.mobile.tskmngmntsystm.service.CommentService;
import com.effective.mobile.tskmngmntsystm.util.CurrentUserUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CurrentUserUtil currentUserUtil;
    private final TaskRepository taskRepository;

    @Override
    public TaskServiceResponse<CommentRs> createComment(CommentRq commentRq) {
        TaskEntity taskById = taskRepository.findById(commentRq.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException("Задача, к которой вы хотите написать комментарий, с id " + commentRq.getTaskId() + " не найдена"));
        Long performerId = taskById.getPerformerId();
        Long currentUserId = currentUserUtil.getCurrentUserId();
        Role currentUserRole = currentUserUtil.getCurrentUserRole();
        if (!performerId.equals(currentUserId) && currentUserRole.equals(Role.ROLE_USER)) {
            log.error("Пользователь не является исполнителем данной задачи");
            return TaskServiceResponse.notOk("вы не являетесь исполнителем данной задачи", HttpStatus.BAD_REQUEST);
        }
        try {
            CommentEntity commentEntity = commentMapper.toCommentEntity(commentRq);
            commentEntity.setAuthorId(currentUserId);
            CommentEntity save = commentRepository.save(commentEntity);
            log.info("Комментарий добавлен. id = {}", save.getId());
            return TaskServiceResponse.ok("Комментарий добавлен", commentMapper.toCommentRs(save));
        } catch (Exception e) {
            log.error("Не удалось добавить комментарий. Error: {}", e.getMessage());
            throw new CustomException("Не удалось добавить комментарий", e);
        }
    }

    @Override
    @Transactional
    public TaskServiceResponse<CommentRs> deleteComment(Long id) {
        CommentEntity commentById = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий, который вы хотите удалить, с id " + id + " не найдена"));
        Long performerId = commentById.getAuthorId();
        Long currentUserId = currentUserUtil.getCurrentUserId();
        Role currentUserRole = currentUserUtil.getCurrentUserRole();
        if (!performerId.equals(currentUserId) && currentUserRole.equals(Role.ROLE_USER)) {
            log.error("Пользователь не является автором данного комментария");
            return TaskServiceResponse.notOk("вы можете удалить только свой комментарий", HttpStatus.BAD_REQUEST);
        }
        try {
            commentRepository.deleteById(id);
            log.info("Комментарий удален. id = {}", id);
            return TaskServiceResponse.ok("Комментарий удален");
        } catch (Exception e) {
            log.error("Не удалось удалить комментарий. id = {}. Error: {}", id, e.getMessage());
            throw new CustomException("Ошибка при удалении", e);
        }
    }
}
