package com.abatalev.pgnch.generator;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
public class ScheduledTasks {

    private Random random = new Random();

    @Scheduled(fixedDelayString = "1000")
    public void runPost() {
        try {
            ActionRequest action = new ActionRequest();
            action.setId("" + random.nextInt(100));
            action.setDelta(1);
            System.out.println("runPost begin " + action.getId());
            ActionResponse response = new RestTemplate().postForObject("http://service:8080/add", action, ActionResponse.class);
            System.out.println("runPost result: " + response.getStatus());
        } catch (Throwable e) {
            System.out.println("runPost error: " + e.getMessage());
        }
    }

    @Scheduled(fixedDelayString = "10000")
    public void runReport() {
        System.out.println("runReport begin");
        try {
            ResponseEntity<Resource> responseEntity = new RestTemplate().exchange("http://reporter:8080/report", HttpMethod.GET, null, Resource.class);
            String filename = responseEntity.getBody().getFilename();
            System.out.println("runReport fileName = " + filename);
            System.out.println("runReport done");
        } catch (Throwable e) {
            System.out.println("runReport error: " + e.getMessage());
        }
    }
}
