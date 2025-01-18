package com.effective.mobile.tskmngmntsystm.controller;

import com.effective.mobile.tskmngmntsystm.dto.TaskRq;
import com.effective.mobile.tskmngmntsystm.dto.TaskRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/task")
@Tag(name = "Работа с задачами")
public interface TaskApi {

    @Operation(
            summary = "Создать задачу",
            description = "Создание новой задачи",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskServiceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    TaskServiceResponse<TaskRs> createTask(@RequestBody TaskRq taskRq);

    @Operation(
            summary = "Редактировать задачу",
            description = "Изменение задачи"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskServiceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    TaskServiceResponse<TaskRs> updateTask(@RequestBody TaskRq taskRq, @PathVariable(name = "id") Long id);

    @Operation(
            summary = "Редактировать статус задачи",
            description = "Изменение статуса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskServiceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @PutMapping("/{id}/status")
    TaskServiceResponse<TaskRs> updateTaskStatus(@RequestParam(name = "newStatus") Status newStatus, @PathVariable(name = "id") Long id);

    @Operation(
            summary = "Удалить задачу",
            description = "Удаление задачи по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskServiceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    TaskServiceResponse<TaskRs> deleteTask(@PathVariable(name = "id") Long id);

    @Operation(
            summary = "Задача по идентификатору",
            description = "Получение задачи по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskServiceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @GetMapping("/{id}")
    TaskServiceResponse<TaskRs> getTaskById(@PathVariable(name = "id") Long id);

    @Operation(
            summary = "Все задачи",
            description = "Получение списка всех задач"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskServiceResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @GetMapping
    TaskServiceResponse<List<TaskRs>> getAllTasks(@RequestParam(name = "authorId", required = false) Long authorId,
                                                  @RequestParam(name = "performerId", required = false) Long performerId,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size);


}
