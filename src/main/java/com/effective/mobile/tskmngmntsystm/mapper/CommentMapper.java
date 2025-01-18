package com.effective.mobile.tskmngmntsystm.mapper;

import com.effective.mobile.tskmngmntsystm.dto.CommentRq;
import com.effective.mobile.tskmngmntsystm.dto.CommentRs;
import com.effective.mobile.tskmngmntsystm.models.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "task.id", source = "commentRq.taskId")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity toCommentEntity(CommentRq commentRq, Long id);

    @Mapping(target = "task.id", source = "commentRq.taskId")
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity toCommentEntity(CommentRq commentRq);

    @Mapping(target = "taskId", source = "task.id")
    CommentRs toCommentRs(CommentEntity commentEntity);
}