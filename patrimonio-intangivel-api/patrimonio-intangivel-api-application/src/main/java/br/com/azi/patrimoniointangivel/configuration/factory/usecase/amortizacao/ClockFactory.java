package br.com.azi.patrimoniointangivel.configuration.factory.usecase.amortizacao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockFactory {

    @Bean("Clock")
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
