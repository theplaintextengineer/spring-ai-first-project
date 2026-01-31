package com.example.spring_ai_first_project.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_ai_first_project.model.Subject;

public interface SubjectRepo extends JpaRepository<Subject, Long> {

}
