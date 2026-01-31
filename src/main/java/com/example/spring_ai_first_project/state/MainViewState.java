package com.example.spring_ai_first_project.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;

import com.example.spring_ai_first_project.model.Subject;
import com.example.spring_ai_first_project.service.subject.SubjectService;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.RequiredArgsConstructor;

@SpringComponent
@Scope("session")
@RequiredArgsConstructor
public class MainViewState {
    private final SubjectService subjectService;
    private boolean isStreamingAiResponse;
    private List<Consumer<Object>> listeners;

    public boolean isStreamingAiResponse() {
        return isStreamingAiResponse;
    }

    public void setStreamingAiResponse(boolean isStreamingAiResponse) {
        this.isStreamingAiResponse = isStreamingAiResponse;
        this.listeners.forEach(c -> c.accept(c));
    }

    public void addListener(Consumer<Object> consumer) {
        synchronized (MainViewState.class) {
            if (Objects.isNull(listeners)) {
                this.listeners = new ArrayList<>();
            }
        }

        this.listeners.add(consumer);
    }

    public List<Subject> getSubjects() {
        return subjectService.getAllSubjects();
    }
}
