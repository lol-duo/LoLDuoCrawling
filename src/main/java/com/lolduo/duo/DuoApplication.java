package com.lolduo.duo;

import com.lolduo.duo.service.RiotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.metrics.export.datadog.EnableDatadogMetrics;

@SpringBootApplication
public class DuoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DuoApplication.class, args);
    }
}
