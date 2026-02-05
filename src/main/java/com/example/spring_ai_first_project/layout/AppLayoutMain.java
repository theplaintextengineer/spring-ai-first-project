package com.example.spring_ai_first_project.layout;

import java.util.Objects;

import org.springframework.context.annotation.Scope;

import com.example.spring_ai_first_project.constant.AppConstant;
import com.example.spring_ai_first_project.state.ChatState;
import com.example.spring_ai_first_project.state.MainViewState;
import com.example.spring_ai_first_project.views.MainView;
import com.example.spring_ai_first_project.views.RecentChatsView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.micrometer.common.util.StringUtils;

@Layout
@SpringComponent
@Scope("prototype")
public class AppLayoutMain extends AppLayout {
  private final Span subjectLabel;
  private final Select<String> dropdown;
  private final MainViewState mainViewState;

  public AppLayoutMain(MainView mainView, RecentChatsView recentChatsView, final MainViewState mainViewState,
      ChatState chatState) {
    this.mainViewState = mainViewState;

    HorizontalLayout header = new HorizontalLayout();
    header.setAlignItems(Alignment.CENTER);
    header.setWidthFull();

    H1 projectTitle = new H1(AppConstant.APP_NAME);
    projectTitle.setWidthFull();

    subjectLabel = new Span();
    subjectLabel.setWidthFull();
    header.expand(subjectLabel);

    dropdown = new Select<String>();
    // dropdown.setItems("Computer Organization", "Computer Networks", "Data
    // Structure", "Operating Systems");
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
    addToDrawer(recentChatsView);
  }

  private void setSubjectLabel(String label) {
    this.subjectLabel.setText(label);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    Objects.requireNonNull(dropdown)
        .setItems(mainViewState.getSubjects()
            .stream()
            .map(s -> s.getName())
            .toList());
  }
}
