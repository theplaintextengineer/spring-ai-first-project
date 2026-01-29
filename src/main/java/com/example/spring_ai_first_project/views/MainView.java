package com.example.spring_ai_first_project.views;

import java.util.Map;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.example.spring_ai_first_project.service.ChatService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputVariant;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

@Route("")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainView extends VerticalLayout {
    private static final String EMPTY_MSG = "";
    private static final String SUBJECT_EXPERT = "Subject Expert";
    private static final String YOU = "You";
    private final ChatService chatService;
    private final Map<String, List<String>> messages;
    private final Scroller scroller;
    private MessageList messageList;

    public MainView(ChatService chatService, UI ui) {
        setSizeFull();
        setPadding(true);
        setSpacing(false);

        this.chatService = chatService;

        this.messages = new HashMap<>();
        this.messages.put(YOU, new LinkedList<>());
        this.messages.put(SUBJECT_EXPERT, new LinkedList<>());

        this.messageList = new MessageList();
        this.messageList.setMarkdown(true);
        this.messageList.setWidthFull();

        scroller = new Scroller(messageList);

        var input = new MessageInput();
        input.setWidthFull();
        input.setTooltipText("Enter the topic you want to revise from Computer Organization Subject:");
        input.addThemeVariants(MessageInputVariant.AURA_ICON_BUTTON);
        input.addSubmitListener(e -> handleUserMessage(e.getValue()));

        expand(scroller);
        add(scroller, input);
    }

    private void handleUserMessage(String userMsg) {
        if (userMsg.isEmpty())
            return;

        var expertMsg = createPlaceholderMessage(true);
        addMessage(userMsg, false);
        addMessage(expertMsg);

        streamResponse(userMsg, expertMsg);
    }

    private void addMessage(String message, boolean isAssistant) {
        var username = isAssistant ? SUBJECT_EXPERT : YOU;
        var item = new MessageListItem(message, Instant.now(), username);
        messageList.addItem(item);
    }

    private void addMessage(MessageListItem item) {
        messageList.addItem(item);
    }

    private MessageListItem createPlaceholderMessage(boolean isAssistant) {
        var username = isAssistant ? SUBJECT_EXPERT : YOU;
        return new MessageListItem(EMPTY_MSG, Instant.now(), username);
    }

    private void streamResponse(String userMsg, MessageListItem targetItem) {
        var fullResponse = new StringBuilder();
        chatService.talkToLlmReactive(userMsg)
                .subscribe(chunk -> {
                    getUI().ifPresent(ui -> ui.access(() -> {
                        fullResponse.append(chunk);
                        targetItem.appendText(chunk);
                        scroller.scrollToBottom();
                    }));
                });
    }

    private void greetResponse(MessageListItem targetItem) {
        var fullResponse = new StringBuilder();
        chatService.greetReactive()
                .subscribe(chunk -> {
                    getUI().ifPresent(ui -> ui.access(() -> {
                        fullResponse.append(chunk);
                        targetItem.appendText(chunk);
                    }));
                });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        var msgItem = createPlaceholderMessage(true);
        addMessage(msgItem);
        greetResponse(msgItem);
    }
}
