package com.pen.scheduler;

import com.pen.candidate.domain.Candidate;
import com.pen.candidate.service.CandidateService;
import com.pen.client.exam.ExamEventSearchFilter;
import com.pen.exam.domain.ExamEvent;
import com.pen.exam.domain.ExamSchedule;
import com.pen.exam.domain.ExamStatus;
import com.pen.exam.service.ExamEventService;
import com.pen.exam.service.ExamScheduleService;
import com.pen.client.otp.OtpRequest;
import com.pen.otp.service.EmailService;
import com.pen.otp.service.OtpService;
import com.pen.request.service.RequestService;
import com.pen.scribe.domain.Scribe;
import com.pen.scribe.service.ScribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static com.pen.request.domain.RequestStatus.ACCEPTED;

@Slf4j
@Service
public class SchedulerService {

    public static final int SCHEDULED_IN_NEXT_HOURS = 1;

    @Autowired
    private ExamEventService examEventService;

    @Autowired
    private ExamScheduleService examScheduleService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ScribeService scribeService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;

    @Scheduled(fixedDelayString = "${pen.exam.schedule.delay-in-ms}", initialDelayString = "${pen.exam.initial-delay-in-ms}")
    public void scheduleOtpGeneration() {
        log.debug("Scheduled OTP generation in progress.");
        // Get all examEvents eligible for OTP generation
        List<ExamEvent> examEvents = getEligibleExamEventsForOTP();
        for (ExamEvent event : examEvents) {
            if (event.getOtpGenerated() == null) {
                OtpRequest otpRequest = new OtpRequest(event.getCandidate(), event.getScribe());
                int otp = otpService.generate(otpRequest);
                if (otp > 0) {
                    sendMail(otpRequest, event, otp);
                }
            }
        }
    }

    @Scheduled(fixedDelayString = "${pen.exam.schedule.delay-in-ms}", initialDelayString = "${pen.exam.initial-delay-in-ms}")
    public void scheduleExamCompletion() {
        log.debug("Scanning exams to mark them for completion.");
        List<ExamEvent> allStartedExams = examEventService.getAllExamsEligibleForCompletion();
        allStartedExams.forEach(e -> {
            e.setExamStatus(ExamStatus.COMPLETED);
            requestService.deleteByExamScheduleAndNotStatus(e.getExamSchedule(), ACCEPTED);
        });
        examEventService.saveAll(allStartedExams);
    }

    private void sendMail(OtpRequest otpRequest, ExamEvent event, int otp) {
        Scribe scribe = scribeService.findById(otpRequest.getScribeId());
        Candidate candidate = candidateService.findById(otpRequest.getCandidateId());
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("otp-notification",
                new String[]{scribe.getFirstName(), String.valueOf(otp),
                        candidate.getFirstName() + " " + candidate.getLastName()}, locale);
        //FIXME: To be uncommented later to send mail.
//        emailService.send(scribe.getEmail(), "OTP for Scribe", message);
        event.setOtpGenerated(true);
        examEventService.save(event);
        ExamSchedule examSchedule = examScheduleService.getById(event.getExamSchedule())
                .orElseThrow(() -> new IllegalStateException("Exam schedule not found for exam event " + event.getId()));
        log.info("OTP sent to {} for the exam scheduled from {} to {}", scribe.getEmail(), examSchedule.getStartTime(),
                examSchedule.getEndTime());
    }

    private List<ExamEvent> getEligibleExamEventsForOTP() {
        ExamEventSearchFilter filter = new ExamEventSearchFilter();
        filter.setScheduledInNextHours(SCHEDULED_IN_NEXT_HOURS);
        return examEventService.search(filter);
    }
}
