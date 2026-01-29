package com.example.spring_ai_first_project.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout{

    public MainView(){
        add(new H1("Welcome, This is my Spring AI + Vaadin Project"));
    }
}
