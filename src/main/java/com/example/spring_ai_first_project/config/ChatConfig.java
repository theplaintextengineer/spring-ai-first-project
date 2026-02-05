package com.example.spring_ai_first_project.config;

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration(proxyBeanMethods = false)
public class ChatConfig {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        var options = ChatOptions.builder()
                .model("llama3.2:3b")
                .temperature(0.2) // Less Creative -> More Deterministic
                .build();

        return builder
                .defaultOptions(options)
                .build();
    }

    @Bean
    Advisor chatAdvisor(final ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor
                .builder(chatMemory)
                .conversationId(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    @Primary
    ChatMemoryRepository chatMemoryRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        return JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(JdbcChatMemoryRepositoryDialect.from(dataSource))
                .build();
    }
}
