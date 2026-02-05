package com.example.spring_ai_first_project.views;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.example.spring_ai_first_project.service.chat.ChatService;
import com.example.spring_ai_first_project.state.ChatState;
import com.example.spring_ai_first_project.state.MainViewState;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputVariant;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.micrometer.common.util.StringUtils;

@Route("")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainView extends VerticalLayout {

  private final ChatState chatState;
  private static final String EMPTY_MSG = "";
  private static final String SUBJECT_EXPERT = "Subject Expert";
  private static final String YOU = "You";
  private final ChatService chatService;
  private final Map<String, List<String>> messages;
  private final Scroller scroller;
  private MessageList messageList;
  private final MainViewState mainViewState;

  public MainView(MainViewState mainViewState, ChatService chatService, ChatState chatState) {
    this.mainViewState = mainViewState;

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
    input.setTooltipText("Enter the topic you want to revise.");
    input.addThemeVariants(MessageInputVariant.AURA_ICON_BUTTON);
    input
        .addSubmitListener(e -> handleUserMessage(chatState.getSubject(), e.getValue(), chatState.getIsFirstMessage()));

    if (StringUtils.isEmpty(chatState.getSubject())) {
      input.setEnabled(false);
    }

    expand(scroller);

    var progressBar = new ProgressBar();
    progressBar.setWidthFull();
    progressBar.getStyle().set("margin-bottom", ".4em");

    add(scroller, progressBar, input);

    chatState.addListener(s -> {
      input.setEnabled(true);

      messages.entrySet().forEach(e -> {
        e.getValue().clear();
      });

      messageList.setItems(new ArrayList<>());

      var newMsgItem = createPlaceholderMessage(true);
      addMessage(newMsgItem);
      greetResponse(s, newMsgItem);
    });

    mainViewState.addListener(_ -> {
      progressBar.setIndeterminate(mainViewState.isStreamingAiResponse());
    });

    this.chatState = chatState;
  }

  private void handleUserMessage(String subject, String userMsg, Boolean isFirstMessage) {
    if (StringUtils.isBlank(userMsg) || StringUtils.isEmpty(subject))
      return;

    var expertMsg = createPlaceholderMessage(true);
    addMessage(userMsg, false);
    addMessage(expertMsg);

    streamResponse(subject, userMsg, expertMsg, isFirstMessage);

    if (isFirstMessage)
      chatState.setIsFirstMessage(false);
  }

  @Deprecated(forRemoval = true)
  private void handleUserMessage(String subject, String userMsg) {
    if (StringUtils.isBlank(userMsg) || StringUtils.isEmpty(subject))
      return;

    var expertMsg = createPlaceholderMessage(true);
    addMessage(userMsg, false);
    addMessage(expertMsg);

    streamResponse(subject, userMsg, expertMsg);
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

  private void streamResponse(String subject, String userMsg, MessageListItem targetItem, Boolean isFirstMessage) {
    messages.get(YOU).add(userMsg);

    var fullResponse = new StringBuilder();
    chatService.talkToLlmReactive(subject, userMsg, isFirstMessage)
        .subscribe(chunk -> {
          getUI().ifPresent(ui -> ui.access(() -> {
            mainViewState.setStreamingAiResponse(true);
            fullResponse.append(chunk);
            targetItem.appendText(chunk);
            scroller.scrollToBottom();
          }));
        }, _ -> {
        }, onAiResponseComplete(fullResponse));
  }

  @Deprecated(forRemoval = true)
  private void streamResponse(String subject, String userMsg, MessageListItem targetItem) {
    messages.get(YOU).add(userMsg);

    var fullResponse = new StringBuilder();
    chatService.talkToLlmReactive(subject, userMsg)
        .subscribe(chunk -> {
          getUI().ifPresent(ui -> ui.access(() -> {
            mainViewState.setStreamingAiResponse(true);
            fullResponse.append(chunk);
            targetItem.appendText(chunk);
            scroller.scrollToBottom();
          }));
        }, _ -> {
        }, onAiResponseComplete(fullResponse));
  }

  private @Nullable Runnable onAiResponseComplete(StringBuilder fullResponse) {
    return () -> {
      getUI().ifPresent(ui -> {
        ui.access(() -> {
          messages.get(SUBJECT_EXPERT).add(fullResponse.toString());
          mainViewState.setStreamingAiResponse(false);
        });
      });
    };
  }

  private void greetResponse(MessageListItem targetItem) {
    var fullResponse = new StringBuilder();
    chatService.greetReactive()
        .subscribe(chunk -> {

          getUI().ifPresent(ui -> ui.access(() -> {
            mainViewState.setStreamingAiResponse(true);

            fullResponse.append(chunk);
            targetItem.appendText(chunk);
          }));
        }, _ -> {
        }, onAiResponseComplete(fullResponse));
  }

  private void greetResponse(String subject, MessageListItem targetItem) {
    var fullResponse = new StringBuilder();
    chatService.greetReactive(subject)
        .subscribe(chunk -> {
          getUI().ifPresent(ui -> ui.access(() -> {
            mainViewState.setStreamingAiResponse(true);
            fullResponse.append(chunk);
            targetItem.appendText(chunk);
          }));
        }, _ -> {
        }, onAiResponseComplete(fullResponse));

  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    var msgItem = createPlaceholderMessage(true);
    addMessage(msgItem);
    greetResponse(msgItem);
  }
}
