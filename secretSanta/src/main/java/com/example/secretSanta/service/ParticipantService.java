package com.example.secretSanta.service;

import com.example.secretSanta.exception.EntityNotExistException;
import com.example.secretSanta.model.entity.Participant;
import com.example.secretSanta.model.request.CreateParticipantRequest;
import com.example.secretSanta.model.response.CreateParticipantResponse;
import com.example.secretSanta.model.response.ParticipantResponse;
import com.example.secretSanta.model.response.ParticipantWithRecipientResponse;
import com.example.secretSanta.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantResponse findParticipantById(Long id) {
        Participant participant = findById(id);
        return new ParticipantResponse(participant.getId(), participant.getName(), participant.getWish());
    }

    private Participant findById(Long id) {
        return participantRepository.findById(id).orElseThrow(
                () -> new EntityNotExistException("Участник с данным айди не существует")
        );
    }

    public ParticipantWithRecipientResponse findParticipantWithRecipient(Long id) {
        Participant participant = findById(id);
        return new ParticipantWithRecipientResponse(participant.getId(), participant.getName(), participant.getWish(),
                findParticipantById(participant.getRecipient()));
    }

    @Transactional
    public CreateParticipantResponse createParticipant(CreateParticipantRequest request) {
        Participant participant = new Participant(null, request.getName(), request.getWish(), null);
        Participant savedParticipant = participantRepository.save(participant);
        return new CreateParticipantResponse(savedParticipant.getId());
    }

    @Transactional
    public List<ParticipantWithRecipientResponse> tossParticipants(List<Participant> participants) {
        Long[] participantsIds = participants.stream()
                .map(Participant::getId)
                .toList().toArray(new Long[0]);
        int i = 0;
        int size = participantsIds.length;
        List<ParticipantWithRecipientResponse> response = new ArrayList<>();
        for (Participant participant : participants) {
            participant.setRecipient(participantsIds[(i + 1) % size]);
            i++;
            response.add(new ParticipantWithRecipientResponse(
                    participant.getId(),
                    participant.getName(),
                    participant.getWish(),
                    findParticipantById(participant.getRecipient())
            ));
        }
        return response;
    }
}
