package mx.utch.dwi.gateway.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfig {

    @Bean
    public Executor compositeExecutor() {
        return Executors.newFixedThreadPool(6);
    }
}
