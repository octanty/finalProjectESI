package com.buildit.Notification;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationRestController {
    @Autowired
    NotificationRepository notificationRepository;

    @PostMapping
    public void notifyPaymentDueDate(@RequestBody Notification notification){

        Notification new_notification = notification;
        notificationRepository.save(new_notification);
    }
}
