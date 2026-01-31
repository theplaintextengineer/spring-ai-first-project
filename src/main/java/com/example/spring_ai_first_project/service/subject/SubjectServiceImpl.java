package com.example.spring_ai_first_project.service.subject;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.spring_ai_first_project.model.Subject;
import com.example.spring_ai_first_project.repo.SubjectRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepo subjectRepo;

    @Override
    public List<Subject> getAllSubjects() {

        return subjectRepo.findAll(
                Sort.by(Sort.Direction.ASC, "name"));
    }

}
