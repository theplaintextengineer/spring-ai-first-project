package com.example.spring_ai_first_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.theme.lumo.Lumo;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@StyleSheet("styles.css")
@PWA(name = "Subject Expert UI", shortName = "Subject Expert")
@Push
public class SpringAiFirstProjectApplication implements AppShellConfigurator {

	static void main(String[] args) {
		SpringApplication.run(SpringAiFirstProjectApplication.class, args);
	}

}
