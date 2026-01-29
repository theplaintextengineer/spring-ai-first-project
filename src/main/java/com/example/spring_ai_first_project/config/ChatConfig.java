package com.example.spring_ai_first_project.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ChatConfig {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        var options = ChatOptions.builder()
                .model("llama3.2:3b")
                .temperature(0.2)
                .build();

        return builder
                .defaultOptions(options)
                .build();
    }
}
