package com.light.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "keycloak")
@Configuration
public class KeycloakProperties {

    private String server;

    private Map<String, String> admin;
}