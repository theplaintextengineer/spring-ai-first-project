package com.example.spring_ai_first_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_ai_first_project.service.chat.ChatService;

@RestController
@RequestMapping(path = "/api/ai")
public class AiController {
  private final ChatService chatService;

  public AiController(ChatService chatService) {
    this.chatService = chatService;
  }

  @GetMapping("/")
  public ResponseEntity<String> talkToSubjectExpert(@RequestParam(name = "topic") String topic) {
    var chatResponse = chatService.talkToLlm(topic);

    return ResponseEntity.ok(chatResponse);
  }
}
