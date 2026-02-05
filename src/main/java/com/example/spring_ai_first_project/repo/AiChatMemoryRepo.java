package com.example.spring_ai_first_project.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_ai_first_project.model.AiChatMemory;

public interface AiChatMemoryRepo extends JpaRepository<AiChatMemory, String> {

}
