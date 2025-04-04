package com.sm.seoulmate.config;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TranslateConfig {
    @Value("${googld.cloud.key}")
    private String apiKey;

    @Bean
    public Translate translateService() {
        return TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
    }
}
