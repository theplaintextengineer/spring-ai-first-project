package com.example.spring_ai_first_project.layout;

import org.springframework.context.annotation.Scope;

import com.example.spring_ai_first_project.views.MainView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.annotation.SpringComponent;

@Layout
@SpringComponent
@Scope("prototype")
public class AppLayoutMain extends AppLayout {
    public AppLayoutMain(MainView mainView) {
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        H1 projectTitle = new H1("The Plaintext Engineer Academy");
        // projectTitle.getStyle()
        //         .set("font-size", "var(--aura-font-size-l)")
        //         .set("margin", "0 var(--aura-space-m)")
        //         .set("color", "var(--aura-primary-text-color)");

        Span subjectLabel = new Span("| COA");

        header.add(projectTitle, subjectLabel);

        addToNavbar(header);
        setContent(mainView);
    }
}
