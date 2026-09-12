package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByPerson_IdOrderByCreatedAtDesc(Integer personId);
}
