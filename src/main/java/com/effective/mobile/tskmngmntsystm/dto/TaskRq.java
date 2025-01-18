package com.effective.mobile.tskmngmntsystm.dto;

import com.effective.mobile.tskmngmntsystm.enums.Priority;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Сущность задачи")
public class TaskRq {

    @Schema(description = "Заголовок задачи", example = "Добавить тесты")
    @Size(min = 5, max = 150, message = "Заголовок задачи должен содержать от 5 до 150 символов")
    @NotBlank(message = "Заголовок задачи не может быть пустыми")
    private String title;

    @Schema(description = "Описание задачи", example = "Напишите несколько базовых тестов для проверки основных функций вашей системы.")
    @Size(min = 5, max = 150, message = "Описание задачи должен содержать от 5 до 150 символов")
    @NotBlank(message = "Описание задачи не может быть пустыми")
    private String description;

    @Schema(description = "Статус задачи", example = "PENDING")
    @NotBlank(message = "Статус задачи не может быть пустыми")
    private Status status;

    @Schema(description = "Приоритет задачи", example = "LOW")
    @NotBlank(message = "Приоритет задачи не может быть пустыми")
    private Priority priority;

    @Schema(description = "Идентификатор исполнителя задачи", example = "1")
    @NotBlank(message = "Идентификатор исполнителя не может быть пустыми")
    private Long performerId;
}
