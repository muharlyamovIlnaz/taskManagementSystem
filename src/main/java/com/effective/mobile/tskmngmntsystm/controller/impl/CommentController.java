package com.effective.mobile.tskmngmntsystm.controller.impl;

import com.effective.mobile.tskmngmntsystm.controller.CommentApi;
import com.effective.mobile.tskmngmntsystm.dto.CommentRq;
import com.effective.mobile.tskmngmntsystm.dto.CommentRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController implements CommentApi {

    private final CommentService commentService;

    @Override
    public TaskServiceResponse<CommentRs> createComment(CommentRq commentRq) {
        log.info("Получен запрос на создание нового комментария");
        return commentService.createComment(commentRq);
    }

    @Override
    public TaskServiceResponse<CommentRs> deleteComment(Long id) {
        log.info("Получен запрос на удаление комментария");
        return commentService.deleteComment(id);
    }
}
