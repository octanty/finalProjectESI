package com.example.demo.schedulingtasks;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.domain.model.InvoiceStatus;
import com.example.demo.invoicing.domain.repository.InvoiceRepository;
import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    NotificationRepository notificationRepository;

    RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:9000/api/notification";

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    // private static final Long day_in_ms = 86400000l;
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        System.out.println(dateFormat.format(new Date()));
        List<Invoice> invoiceList = invoiceRepository.findAll();
        for (Invoice invoice : invoiceList) {
            if (invoice.getStatus() == InvoiceStatus.PENDING &&
                invoice.getDueDate().isEqual(LocalDate.now().plusDays(2)))
            {
                Notification notification = new Notification();
                notification.setPo_id(invoice.getPurchaseOrderId());
                notification.setMessage("PAY!!!!!!");
                notificationRepository.save(notification);
                restTemplate.postForObject(BASE_URL, notification, Notification.class);

                System.out.println(invoice);
            }
        }

    }
}
