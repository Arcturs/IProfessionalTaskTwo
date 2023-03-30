package com.example.secretSanta.service;

import com.example.secretSanta.exception.EntityNotExistException;
import com.example.secretSanta.exception.InvalidTossException;
import com.example.secretSanta.model.entity.Participant;
import com.example.secretSanta.model.request.CreateGroupRequest;
import com.example.secretSanta.model.request.CreateParticipantRequest;
import com.example.secretSanta.model.request.UpdateGroupRequest;
import com.example.secretSanta.model.response.*;
import com.example.secretSanta.model.entity.Group;
import com.example.secretSanta.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final ParticipantService participantService;

    @Transactional
    public CreateGroupResponse createGroup(CreateGroupRequest request) {
        Group group = new Group(null, request.getName(), request.getDescription(), null);
        Group savedGroup = groupRepository.save(group);
        return new CreateGroupResponse(savedGroup.getId());
    }

    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(group -> new GroupResponse(group.getId(), group.getName(), group.getDescription()))
                .toList();
    }

    public GroupInfoResponse getGroupById(Long id) {
        Group group = findGroupById(id);
        return new GroupInfoResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getParticipants().stream()
                        .map(participant -> new ParticipantWithRecipientResponse(
                                participant.getId(),
                                participant.getName(),
                                participant.getWish(),
                                participantService.findParticipantById(participant.getRecipient())
                        ))
                        .toList());
    }

    private Group findGroupById(Long id) {
        return groupRepository.findById(id).orElseThrow(
                () -> new EntityNotExistException("Группа с данным айди не существует")
        );
    }

    @Transactional
    public void updateGroup(UpdateGroupRequest request, Long id) {
        Group group = findGroupById(id);
        group.setDescription(request.getDescription());
        group.setName(request.getName() == null || request.getName().isEmpty()
                ? group.getName()
                : request.getName());
        groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = findGroupById(id);
        groupRepository.delete(group);
    }

    @Transactional
    public CreateParticipantResponse createAndAddParticipantToGroup(Long id, CreateParticipantRequest request) {
        Group group = findGroupById(id);
        CreateParticipantResponse response = participantService.createParticipant(request);
        groupRepository.addParticipantToGroup(group.getId(), response.getId());
        return response;
    }

    @Transactional
    public void deleteParticipantFromGroup(Long groupId, Long participantId) {
        Group group = findGroupById(groupId);
        ParticipantResponse participantIdFromService = participantService.findParticipantById(participantId);
        List<Long> participantsIds = group.getParticipants().stream()
                .map(Participant::getId)
                .filter(id -> id.equals(participantIdFromService.getId()))
                .toList();
        if (participantsIds.isEmpty()) {
            throw new EntityNotExistException("Участник не найден в группе");
        }
        Pair<Long, Long> ids = Pair.of(group.getId(), participantIdFromService.getId());
        groupRepository.deleteParticipantFromGroup(ids.getFirst(), ids.getSecond());
    }

    public ParticipantResponse findRecipientFromGroupAndParticipant(Long groupId, Long participantId) {
        Group group = findGroupById(groupId);
        ParticipantWithRecipientResponse participant =
                participantService.findParticipantWithRecipient(participantId);
        List<Long> participantsIds = group.getParticipants().stream()
                .map(Participant::getId)
                .filter(id -> id.equals(participant.getId()))
                .toList();
        if (participantsIds.isEmpty()) {
            throw new EntityNotExistException("Участник не найден в группе");
        }
        return participant.getRecipient();
    }

    @Transactional
    public List<ParticipantWithRecipientResponse> tossParticipants(Long id) {
        Group group = findGroupById(id);
        if (group.getParticipants().size() < 3) {
            throw new InvalidTossException("Проведение жеребьевки невозможно, т.к. меньше 3 участников в группе");
        }
        return participantService.tossParticipants(group.getParticipants());
    }
}
