package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByPerson_IdOrderByCreatedAtDesc(Integer personId);

    Optional<Notification> findByIdAndPerson_Id(Integer id, Integer personId);

    //borrados en bloque (un solo DELETE, sin cargar las entidades); siempre filtrados por el dueno
    @Modifying
    @Query("delete from Notification n where n.person.id = :personId and n.id in :ids")
    int deleteByOwnerAndIds(@Param("personId") Integer personId, @Param("ids") Collection<Integer> ids);

    @Modifying
    @Query("delete from Notification n where n.person.id = :personId")
    int deleteAllByOwner(@Param("personId") Integer personId);

    @Modifying
    @Query("delete from Notification n where n.person.id = :personId and n.read = true")
    int deleteReadByOwner(@Param("personId") Integer personId);

    //limpieza automatica: solo las leidas y viejas
    @Modifying
    @Query("delete from Notification n where n.read = true and n.createdAt < :before")
    int deleteReadOlderThan(@Param("before") LocalDateTime before);
}
