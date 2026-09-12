package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Service.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
public class NotificationController {

    private final INotificationService notificationService;

    @GetMapping("/notifications/{personId}")
    public ResponseEntity<?> getNotifications(@PathVariable Integer personId) {
        List<NotificationResponse> notifications = notificationService.getNotifications(personId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Integer id) {
        return new ResponseEntity<>(notificationService.markAsRead(id), HttpStatus.OK);
    }
}
