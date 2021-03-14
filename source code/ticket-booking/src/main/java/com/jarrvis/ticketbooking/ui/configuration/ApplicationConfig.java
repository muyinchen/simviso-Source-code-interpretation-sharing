package com.jarrvis.ticketbooking.ui.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationConfig {


    @NotNull
    private WebAccess webAccess;

    @Data
    public static class WebAccess {
        @NotEmpty
        private String username;

        @NotEmpty
        private String password;
    }

}
