package com.example.spring_ai_first_project.layout;

import org.springframework.context.annotation.Scope;

import io.micrometer.common.util.StringUtils;

import com.example.spring_ai_first_project.state.ChatState;
import com.example.spring_ai_first_project.views.MainView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.annotation.SpringComponent;

@Layout
@SpringComponent
@Scope("prototype")
public class AppLayoutMain extends AppLayout {
    private final Span subjectLabel;

    public AppLayoutMain(MainView mainView, ChatState chatState) {
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        header.setWidthFull();

        H1 projectTitle = new H1("The Plaintext Engineer Academy");
        projectTitle.setWidthFull();

        subjectLabel = new Span();
        subjectLabel.setWidthFull();
        header.expand(subjectLabel);

        var dropdown = new Select<String>();
        dropdown.setItems("Computer Organization", "Computer Networks", "Data Structure", "Operating Systems");
        dropdown.setEmptySelectionCaption("Select Subject");

        dropdown.setEmptySelectionAllowed(true);

        dropdown.addValueChangeListener(event -> {

            var selectedSubject = event.getValue();

            if (StringUtils.isEmpty(selectedSubject)) {
                return;
            }

            chatState.setSubject(selectedSubject);
            setSubjectLabel(selectedSubject);
        });

        header.add(projectTitle, subjectLabel, dropdown);

        addToNavbar(header);
        setContent(mainView);
    }

    private void setSubjectLabel(String label) {
        this.subjectLabel.setText(label);
    }
}
