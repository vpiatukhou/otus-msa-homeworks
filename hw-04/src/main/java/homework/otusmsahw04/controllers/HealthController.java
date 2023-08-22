package homework.otusmsahw04.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private static final Map<String, String> HEALTH_STATUS_RESPONSE = Map.of("status", "OK");

    @GetMapping("/health")
    public Map<String, String> getHealthStatus() {
        return HEALTH_STATUS_RESPONSE;
    }
}
