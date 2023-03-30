package com.example.secretSanta.controller;

import com.example.secretSanta.model.ExceptionDTO;
import com.example.secretSanta.model.request.CreateGroupRequest;
import com.example.secretSanta.model.request.CreateParticipantRequest;
import com.example.secretSanta.model.request.UpdateGroupRequest;
import com.example.secretSanta.model.response.*;
import com.example.secretSanta.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/groups")
    @Operation(description = "Возвращает все группы", responses = {
            @ApiResponse(responseCode = "200", description = "Группы найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(allOf = GroupResponse.class)))
    })
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/groups/{id}")
    @Operation(description = "Возвращает группу с участниками", responses = {
            @ApiResponse(responseCode = "200", description = "Группа найдена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найдена группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> getGroupById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping("/group")
    @Operation(description = "Создает группу", responses = {
            @ApiResponse(responseCode = "201", description = "Группа успешно создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateGroupResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> createGroup(@RequestBody @Valid CreateGroupRequest request) {
        return ResponseEntity.status(CREATED).body(groupService.createGroup(request));
    }

    @PutMapping("/group/{id}")
    @Operation(description = "Обновление группы", responses = {
            @ApiResponse(responseCode = "202", description = "Группа обновлена",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найдена группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> updateGroup(@PathVariable("id") Long id,
                                         @RequestBody @Valid UpdateGroupRequest request) {
        groupService.updateGroup(request, id);
        return ResponseEntity.status(ACCEPTED).build();
    }

    @DeleteMapping("/group/{id}")
    @Operation(description = "Удаление группы", responses = {
            @ApiResponse(responseCode = "204", description = "Группа удалена",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найдена группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/group/{id}/participant")
    @Operation(description = "Добавление нового участника в группа", responses = {
            @ApiResponse(responseCode = "201", description = "Участник успешно добавлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateParticipantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найдена группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> createParticipant(@PathVariable("id") Long id,
                                               @RequestBody @Valid CreateParticipantRequest request) {
        return ResponseEntity.status(CREATED).body(groupService.createAndAddParticipantToGroup(id, request));
    }

    @PostMapping("/group/{id}/toss")
    @Operation(description = "Проводит жеребьевку", responses = {
            @ApiResponse(responseCode = "200", description = "Жеребьевка проведена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(allOf = ParticipantWithRecipientResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найдена группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Жеребьевка не может быть проведена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> toss(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groupService.tossParticipants(id));
    }

    @DeleteMapping("/group/{groupId}/participant/{participantId}")
    @Operation(description = "Удаление участника из группы", responses = {
            @ApiResponse(responseCode = "204", description = "Участник удален из группы",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найден участник или группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> deleteParticipantFromGroup(@PathVariable("groupId") Long groupId,
                                                        @PathVariable("participantId") Long participantId) {
        groupService.deleteParticipantFromGroup(groupId, participantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/{groupId}/participant/{participantId}/recipient")
    @Operation(description = "Нахождение подопечного участника группы", responses = {
            @ApiResponse(responseCode = "200", description = "Подопечный найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные входные параметры",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Не найден участник или группа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<?> getRecipientFromGroupAndParticipant(@PathVariable("groupId") Long groupId,
                                                                 @PathVariable("participantId") Long participantId) {
        return ResponseEntity.ok(groupService.findRecipientFromGroupAndParticipant(groupId, participantId));
    }
}
