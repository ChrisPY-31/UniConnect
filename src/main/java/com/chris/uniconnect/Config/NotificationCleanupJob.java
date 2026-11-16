package com.chris.uniconnect.Config;

import com.chris.uniconnect.Service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//para que la tabla notification no crezca para siempre: cada dia borra las LEIDAS con mas de 30 dias (las no leidas no se tocan)
@Slf4j
@Configuration
@EnableScheduling
public class NotificationCleanupJob {

    private static final int DAYS_TO_KEEP_READ = 30;

    private final INotificationService notificationService;

    public NotificationCleanupJob(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //3:00 am hora de Mexico, para no depender de la zona del servidor
    @Scheduled(cron = "0 0 3 * * *", zone = "America/Mexico_City")
    public void deleteOldReadNotifications() {
        int deleted = notificationService.deleteReadOlderThan(DAYS_TO_KEEP_READ);
        if (deleted > 0) {
            log.info("Limpieza de notificaciones: {} leidas con mas de {} dias borradas", deleted, DAYS_TO_KEEP_READ);
        }
    }
}
