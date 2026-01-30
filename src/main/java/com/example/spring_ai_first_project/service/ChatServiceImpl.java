package com.example.spring_ai_first_project.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {
  Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

  private final ChatClient chatClient;

  public ChatServiceImpl(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @Override
  public String talkToLlm(String topic) {
    log.info("Starting to talk on Topic: {}", topic);

    return chatClient
        .prompt("You are a Computer Organization subject expert. Summarize the topic in 5 bullet points on "
            + topic)
        .call()
        .content();
  }

  @Override
  public Flux<String> talkToLlmReactive(String topic) {
    log.info("Starting to talk on Topic: {}", topic);

    var params = new HashMap<String, Object>();
    params.put("topic", topic);

    var template = "You are a Computer Organization subject expert. Summarize the topic in 5 bullet points on {topic}";

    var promptTemplate = PromptTemplate.builder()
        .template(template)
        .variables(params)
        .build();

    var prompt = Prompt.builder()
        .content(promptTemplate.render())
        .build();

    return chatClient
        .prompt(prompt)
        .stream()
        .content();
  }

  @Override
  public Flux<String> greetReactive() {
    log.info("Starting to Greet messages.");

    return chatClient
        .prompt(
            "You are a Computer Organization, Computer Networks, Data Structure, Operating Systems subject expert. You can ONLY summarize any topic on these subject into 5 bullets points. This makes revision easy for any student. Introduce yourself as helpful assistant as 'Subject Expert' to user in less than 10-15 words. Don't use any person or place name. Ask user to select any of these subject to get started at top right of the screen dropdown.")
        .stream()
        .content();
  }

  @Override
  public Flux<String> talkToLlmReactive(String subject, String topic) {
    log.info("Starting to talk on Subject: {}, Topic: {}", subject, topic);

    var params = new HashMap<String, Object>();
    params.put("subject", subject);
    params.put("topic", topic);

    var template = "You are a {subject} subject expert. Summarize the topic in 5 bullet points on {topic}. Don't answer topic if it is not related to subject. Simply, say sorry to user.";
    var promptTemplate = PromptTemplate.builder()
        .template(
            template)
        .variables(params)
        .build();

    var prompt = Prompt.builder()
        .content(promptTemplate.render())
        .build();

    return chatClient
        .prompt(prompt)
        .stream()
        .content();
  }

  @Override
  public Flux<String> greetReactive(String subject) {
    log.info("Starting to talk on Subject: {}", subject);

    var template = "You are a {subject} expert. You can ONLY summaeize any topic on {subject} into 5 bullets points. This makes revision easy for any student. Introduce yourself as helpful assistant cum 'Subject Expert' to user in less than 10-15 words. Don't use any person or place name.";

    var params = new HashMap<String, Object>();
    params.put("subject", subject);

    var promptTemplate = PromptTemplate.builder()
        .template(template)
        .variables(params)
        .build();

    var prompt = Prompt.builder()
        .content(promptTemplate.render())
        .build();

    return chatClient
        .prompt(prompt)
        .stream()
        .content();
  }

}
