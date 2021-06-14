package com.light.exam.service;

import com.light.exam.domain.ExamSchedule;
import com.light.request.repository.ExamScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamScheduleService {

    @Autowired
    private ExamScheduleRepository scheduleRepository;

    public Optional<ExamSchedule> getById(long id) {
        return scheduleRepository.findById(id);
    }
}
