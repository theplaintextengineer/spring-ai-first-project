package com.example.spring_ai_first_project.service.chat;

import reactor.core.publisher.Flux;

public interface ChatService {
    String talkToLlm(String topic);

    Flux<String> talkToLlmReactive(String topic);

    Flux<String> talkToLlmReactive(String subject, String topic);

    Flux<String> greetReactive();

    Flux<String> greetReactive(String subject);
}
