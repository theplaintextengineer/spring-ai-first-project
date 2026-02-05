package com.example.spring_ai_first_project.service.chat;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.example.spring_ai_first_project.model.Subject;
import com.example.spring_ai_first_project.service.subject.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

        private final ChatClient chatClient;
        private final SubjectService subjectService;
        private final Advisor chatAdvisor;

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

                // var params = new HashMap<String, Object>();
                // params.put("topic", topic);

                // var template = "You are a Computer Organization subject expert. Summarize the
                // topic in 5 bullet points on {topic}";

                // var promptTemplate = PromptTemplate.builder()
                // .template(template)
                // .variables(params)
                // .build();

                // var prompt = Prompt.builder()
                // .content(promptTemplate.render())
                // .build();

                return chatClient
                                .prompt(topic)
                                .advisors(chatAdvisor)
                                .stream()
                                .content();
        }

        @Override
        public Flux<String> greetReactive() {
                log.info("Starting to Greet messages.");

                var template = "You are a {subjects} subject expert. You can ONLY summarize any topic on these subject into 5 bullets points. This makes revision easy for any student. Introduce yourself as helpful assistant as 'Subject Expert' to user in less than 10-15 words. Don't use any person or place name. Ask user to select any of these subject to get started at top right of the screen dropdown.";

                var variables = new HashMap<String, Object>();

                var subjectsName = subjectService.getAllSubjects()
                                .stream()
                                .map(Subject::getName)
                                .collect(Collectors.joining(","));

                variables.put("subjects", subjectsName);

                var promptTemplate = PromptTemplate.builder()
                                .template(template)
                                .variables(variables)
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
        public Flux<String> talkToLlmReactive(String subject, String topic) {
                log.info("Starting to talk on Subject: {}, Topic: {}", subject, topic);

                var params = new HashMap<String, Object>();
                params.put("subject", subject);
                params.put("topic", topic);

                var template = """
                                You are an expert in {subject}.
                                Your task is to summarize the topic "{topic}" in exactly 5 concise bullet points.

                                Guidelines:
                                - Each bullet point should be clear, factual, and easy to understand.
                                - Do not provide information if the topic is unrelated to {subject}.
                                - If unrelated, respond only with: "Sorry, the topic is not related to {subject}."
                                - Avoid long paragraphs; keep each bullet point under 20 words.
                                """;

                var promptTemplate = PromptTemplate.builder()
                                .template(template)
                                .variables(params)
                                .build();

                var prompt = Prompt.builder()
                                .content(promptTemplate.render())
                                .build();

                return chatClient
                                .prompt(prompt)
                                .advisors(chatAdvisor)
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

        @Override
        public Flux<String> talkToLlmReactive(String subject, String topic, Boolean isFirstMessage) {
                if (isFirstMessage)
                        return talkToLlmReactive(subject, topic);

                return talkToLlmReactive(topic);
        }

}
