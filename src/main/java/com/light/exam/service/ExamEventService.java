package com.light.exam.service;

import com.light.exam.client.ExamEventSearchFilter;
import com.light.exam.client.ExamEventUpdate;
import com.light.exam.client.FeedbackDetails;
import com.light.exam.client.UserType;
import com.light.exam.domain.CandidateFeedback;
import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ScribeFeedback;
import com.light.exam.repository.ExamEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ExamEventService {

    @Autowired
    private ExamEventRepository examEventRepository;

    public ExamEvent create(ExamEventUpdate examEventUpdate) {

        var examEvent = new ExamEvent(examEventUpdate.getRequestId(), examEventUpdate.getCandidate(),
                examEventUpdate.getScribe(), examEventUpdate.getExamSchedule());

        return examEventRepository.save(examEvent);
    }

    public void delete(long requestId) {
        Optional<ExamEvent> examEventOptional = examEventRepository.findByRequest(requestId);
        examEventOptional.ifPresent(e -> examEventRepository.delete(e));
    }

    public List<ExamEvent> search(ExamEventSearchFilter filter) {
        return examEventRepository.search(filter.getCandidate(), filter.getScribe(), filter.getScheduledInNextHours());
    }

    public Optional<ExamEvent> getByRequestId(long requestId) {
        return examEventRepository.findByRequest(requestId);
    }

    public Optional<ExamEvent> getById(long id) {
        return examEventRepository.findById(id);
    }

    public void updateFeedback(long examEventId, FeedbackDetails feedbackDetails) {
        ExamEvent examEvent = getById(examEventId)
                .orElseThrow(()
                        -> new IllegalArgumentException(String.format("Exam event with id {%d} not found!", examEventId)));

        if (feedbackDetails.getUserType() == UserType.CANDIDATE) {
            CandidateFeedback candidateFeedback = new CandidateFeedback();
            candidateFeedback.setRating(feedbackDetails.getRating());
            candidateFeedback.setComment(feedbackDetails.getComment());
            examEvent.setCandidateFeedback(candidateFeedback);
        } else if (feedbackDetails.getUserType() == UserType.SCRIBE) {
            ScribeFeedback scribeFeedback = new ScribeFeedback();
            scribeFeedback.setRating(feedbackDetails.getRating());
            scribeFeedback.setComment(feedbackDetails.getComment());
            examEvent.setScribeFeedback(scribeFeedback);
        } else {
            throw new IllegalArgumentException("Feedback should correspond to either scribe or candidate!");
        }
        examEventRepository.save(examEvent);
    }

    public ExamEvent save(ExamEvent event) {
        return examEventRepository.save(event);
    }

    public Iterable<ExamEvent> saveAll(Iterable<ExamEvent> events) {
        return examEventRepository.saveAll(events);
    }

    public List<ExamEvent> getAllExamsEligibleForCompletion() {
        return examEventRepository.getAllExamsEligibleForCompletion();
    }

}