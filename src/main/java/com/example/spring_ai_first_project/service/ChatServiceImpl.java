package com.example.spring_ai_first_project.service;

import java.util.HashMap;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String talkToLlm(String topic) {
        return chatClient
                .prompt("You are a Computer Organization subject expert. Summarize the topic in 5 bullet points on "
                        + topic)
                .call()
                .content();
    }

    @Override
    public Flux<String> talkToLlmReactive(String topic) {
        var params = new HashMap<String, Object>();
        params.put("topic", topic);

        var promptTemplate = PromptTemplate.builder()
                .template(
                        "You are a Computer Organization subject expert. Summarize the topic in 5 bullet points on {topic}")
                .variables(params)
                .build();

        var prompt = Prompt.builder()
                .content(promptTemplate.render())
                .build();

        return chatClient
                .prompt(prompt)
                .stream()
                .content();
    }

    @Override
    public Flux<String> greetReactive() {
        return chatClient
                .prompt("You are a Computer Organization subject expert. You can ONLY summaeize any topic on Computer Organization subject into 5 bullets points. This makes revision easy for any student. Introduce yourself as helpful assistant cum 'Subject Expert' to user in less than 10-15 words. Don't use any person or place name.")
                .stream()
                .content();
    }

}
