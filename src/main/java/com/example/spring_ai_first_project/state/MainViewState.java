package com.example.spring_ai_first_project.state;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("session")
public class MainViewState {
    private boolean isStreamingAiResponse;
    private final List<Consumer<Object>> listeners;

    public MainViewState(){
        this.listeners = new LinkedList<>();
    }

    public boolean isStreamingAiResponse() {
        return isStreamingAiResponse;
    }

    public void setStreamingAiResponse(boolean isStreamingAiResponse) {
        this.isStreamingAiResponse = isStreamingAiResponse;
        this.listeners.forEach(c->c.accept(c));
    }

    public void addListener(Consumer<Object> consumer){
        this.listeners.add(consumer);
    }
}
