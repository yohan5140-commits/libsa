package com.libsa.backend.popularbook;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "data4library")
public record Data4LibraryProperties(String baseUrl, String authKey, String defaultLibCode) {
}
