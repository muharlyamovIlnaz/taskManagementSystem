package com.effective.mobile.tskmngmntsystm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Сущность комментария")
public class CommentRs {

    @Schema(description = "Идентификатор комментария")
    private Long id;

    @Schema(description = "Текст комментария")
    private String text;

    @Schema(description = "Время создания")
    private LocalDateTime createdAt;

    @Schema(description = "Время изменения")
    private LocalDateTime updatedAt;

    @Schema(description = "Идентификатор автора")
    private Long authorId;

    @Schema(description = "Идентификатор задачи")
    private Long taskId;
}
