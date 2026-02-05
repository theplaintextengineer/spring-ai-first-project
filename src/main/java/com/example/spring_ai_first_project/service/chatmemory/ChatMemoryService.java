package com.example.spring_ai_first_project.service.chatmemory;

import java.util.List;

import com.example.spring_ai_first_project.model.AiChatMemory;

public interface ChatMemoryService {
  List<AiChatMemory> listAllChats();
}