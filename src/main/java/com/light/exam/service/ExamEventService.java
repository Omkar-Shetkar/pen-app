package com.light.exam.service;

import com.light.exam.client.ExamEventUpdate;
import com.light.exam.domain.ExamEvent;
import com.light.exam.repository.ExamEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ExamEventService {

    @Autowired
    private ExamEventRepository examEventRepository;

    public ExamEvent create(ExamEventUpdate examEventUpdate) {

        ExamEvent examEvent = new ExamEvent(examEventUpdate.getCandidate(), examEventUpdate.getScribe(),
                examEventUpdate.getExamSchedule());

        return examEventRepository.save(examEvent);
    }
}