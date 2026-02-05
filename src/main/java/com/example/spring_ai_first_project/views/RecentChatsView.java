package com.example.spring_ai_first_project.views;

import java.util.Objects;

import org.springframework.context.annotation.Scope;

import com.example.spring_ai_first_project.model.AiChatMemory;
import com.example.spring_ai_first_project.service.chatmemory.ChatMemoryService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.spring.annotation.SpringComponent;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@SpringComponent
@RequiredArgsConstructor
@Scope("prototype")
public class RecentChatsView extends SideNav {
  private final ChatMemoryService chatMemoryService;

  @PostConstruct
  void init() {
    setLabel("Recent Chats");
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    Objects.requireNonNull(this)
        .addItem(chatMemoryService.listAllChats()
            .stream()
            .map(AiChatMemory::getConversationId)
            .distinct()
            .map(SideNavItem::new)
            .toArray(SideNavItem[]::new));
  }
}
