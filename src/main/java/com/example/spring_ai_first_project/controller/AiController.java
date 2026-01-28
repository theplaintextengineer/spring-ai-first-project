package com.example.spring_ai_first_project.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
  private final ChatClient chatClient;

  public AiController(ChatClient.Builder builder){
    var options = ChatOptions.builder()
        .model("llama3.2:3b")
        .temperature(0.2)
        .build();

    chatClient = builder
        .defaultOptions(options)
        .build();
  }

  @GetMapping("/")
  public ResponseEntity<String> talkToSubjectExpert(@RequestParam(name = "topic") String topic){
    var chatResponse = chatClient
        .prompt("You are a Computer Organization subject expert. Summarize the topic in 5 bullet points on " + topic)
        .call()
        .content();

    return ResponseEntity.ok(chatResponse);
  }
}
