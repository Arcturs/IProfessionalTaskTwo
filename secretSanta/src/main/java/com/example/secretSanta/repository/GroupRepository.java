package com.example.secretSanta.repository;

import com.example.secretSanta.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO group_participant(group_id, participant_id)
            VALUES(:groupId, :participantId)
            """, nativeQuery = true)
    void addParticipantToGroup(@Param("groupId") Long groupId,
                               @Param("participantId") Long participantId);

    @Modifying
    @Query(value = """
            DELETE group_participant
            WHERE group_id = :groupId
                AND participant_id = :participantId
            """, nativeQuery = true)
    void deleteParticipantFromGroup(@Param("groupId") Long groupId,
                                    @Param("participantId") Long participantId);
}
