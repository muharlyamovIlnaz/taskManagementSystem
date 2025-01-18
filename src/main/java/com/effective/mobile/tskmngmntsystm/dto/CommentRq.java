package com.effective.mobile.tskmngmntsystm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Сущность комментария")
public class CommentRq {

    @Schema(description = "Текст комментария", example = "Переделать, все не то и все не так когда твоя девушка больна")
    @Size(min = 2, max = 150, message = "Комментарий должен содержать от 2 до 150 символов")
    @NotBlank(message = "Комментарий не может быть пустыми")
    private String text;

    @Schema(description = "Идентификатор задачи", example = "1")
    @NotNull(message = "Идентификатор задачи не может быть пустыми")
    private Long taskId;
}
