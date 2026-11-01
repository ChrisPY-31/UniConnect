package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByIdStudentAndIdRecruiter(Integer idStudent, Integer idRecruiter);

    List<Conversation> findByIdStudentOrderByLastMessageAtDesc(Integer idStudent);

    List<Conversation> findByIdRecruiterOrderByLastMessageAtDesc(Integer idRecruiter);
}
