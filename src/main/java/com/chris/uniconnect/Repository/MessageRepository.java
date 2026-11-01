package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.IssuerType;
import com.chris.uniconnect.Model.Entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByIdConversationOrderBySentAtDesc(Long idConversation, Pageable pageable);

    Optional<Message> findFirstByIdConversationOrderBySentAtDesc(Long idConversation);

    //no leidos por conversacion: los que mando la otra parte y no tienen readAt; filas [idConversation, count]
    @Query("select m.idConversation, count(m) from Message m where m.idConversation in :ids " +
            "and m.senderType <> :readerType and m.readAt is null group by m.idConversation")
    List<Object[]> countUnreadByConversation(@Param("ids") Collection<Long> ids, @Param("readerType") IssuerType readerType);

    @Modifying
    @Query("update Message m set m.readAt = :readAt where m.idConversation = :idConversation " +
            "and m.senderType <> :readerType and m.readAt is null")
    int markAsRead(@Param("idConversation") Long idConversation, @Param("readerType") IssuerType readerType, @Param("readAt") LocalDateTime readAt);
}
