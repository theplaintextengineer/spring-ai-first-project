package com.example.spring_ai_first_project.state;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("session")
public class ChatState {
    private String subject;
    private final List<Consumer<String>> listeners;

    public ChatState(){
        listeners = new ArrayList<>();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        this.listeners.forEach(c->c.accept(subject));
    }

    public void addListener(Consumer<String> consumer){
        this.listeners.add(consumer);
    }
}
