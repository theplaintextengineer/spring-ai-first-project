package com.example.spring_ai_first_project.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "spring_ai_chat_memory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiChatMemory {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String conversationId;
  private String content;
  private String type;
  private Instant timestamp;
}
