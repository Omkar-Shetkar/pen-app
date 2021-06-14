package com.light.scheduler;

import com.light.candidate.domain.Candidate;
import com.light.candidate.service.CandidateService;
import com.light.exam.client.ExamEventSearchFilter;
import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ExamSchedule;
import com.light.exam.domain.ExamStatus;
import com.light.exam.service.ExamEventService;
import com.light.exam.service.ExamScheduleService;
import com.light.otp.client.OtpRequest;
import com.light.otp.service.EmailService;
import com.light.otp.service.OtpService;
import com.light.request.service.RequestService;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static com.light.request.domain.RequestStatus.ACCEPTED;

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

    @Scheduled(fixedDelayString = "${light.exam.schedule.delay-in-ms}", initialDelayString = "${light.exam.initial-delay-in-ms}")
    public void scheduleOtpGeneration() {
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

    @Scheduled(fixedDelayString = "${light.exam.schedule.delay-in-ms}", initialDelayString = "${light.exam.initial-delay-in-ms}")
    public void scheduleExamCompletion() {
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
