package com.effective.mobile.tskmngmntsystm.dto;

import com.effective.mobile.tskmngmntsystm.enums.Priority;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskRs {

    @Schema(description = "Идентификкатор задачи")
    private Long id;

    @Schema(description = "Заголовок задачи")
    private String title;

    @Schema(description = "Описание задачи")
    private String description;

    @Schema(description = "Статус задачи")
    private Status status;

    @Schema(description = "Приоритет задачи")
    private Priority priority;

    @Schema(description = "Идентификатор автора задачи")
    private Long authorId;

    @Schema(description = "Идентификатор исполнителя задачи")
    private Long performerId;

    @Schema(description = "Список комментариев")
    private List<CommentRs> comments;

    @Schema(description = "Время создания")
    private LocalDateTime createdAt;

    @Schema(description = "Время изменения")
    private LocalDateTime updatedAt;

}
