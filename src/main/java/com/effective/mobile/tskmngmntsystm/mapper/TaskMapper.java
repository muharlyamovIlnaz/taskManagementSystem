package com.effective.mobile.tskmngmntsystm.mapper;

import com.effective.mobile.tskmngmntsystm.dto.TaskRq;
import com.effective.mobile.tskmngmntsystm.dto.TaskRs;
import com.effective.mobile.tskmngmntsystm.models.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", source = "id")
    TaskEntity toTaskEntity(TaskRq taskRq, Long id);

    @Mapping(target = "id", ignore = true)
    TaskEntity toTaskEntity(TaskRq taskRq);

    TaskRs toTaskRs(TaskEntity taskEntity);
}
