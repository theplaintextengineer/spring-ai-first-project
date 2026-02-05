package com.example.spring_ai_first_project.service.chatmemory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring_ai_first_project.model.AiChatMemory;
import com.example.spring_ai_first_project.repo.AiChatMemoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMemoryServiceImpl implements ChatMemoryService {
  private final AiChatMemoryRepo chatMemoryRepo;

  @Override
  public List<AiChatMemory> listAllChats() {
    return chatMemoryRepo.findAll();
  }

}
