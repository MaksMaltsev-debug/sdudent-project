package com.maltsev.scheduler.quartz.service.impl;

import com.maltsev.cross.request.JobRequest;
import com.maltsev.scheduler.exception.JobSchedulingException;
import com.maltsev.scheduler.quartz.jobdetails.JobDetailFactory;
import com.maltsev.scheduler.quartz.service.JobService;
import com.maltsev.scheduler.quartz.trigger.TriggerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final Scheduler scheduler;
    private final TriggerFactory triggerFactory;
    private final JobDetailFactory jobDetailFactory;

    @Override
    @Transactional
    public JobRequest createJob(JobRequest jobRequest) {
        log.info("Creating quartz job job request = {}", jobRequest);

        Set<Trigger> triggers = buildTriggers(jobRequest);
        JobDetail jobDetail = buildJobDetail(jobRequest);

        try {
            scheduler.scheduleJob(jobDetail, triggers, true);
            log.info("Job with a key = {} is successfully persisted.", jobDetail.getKey());
        } catch (SchedulerException e) {
            log.error("Job with a key = {} could not be persisted, reason = {}", jobDetail.getKey(), e.getMessage());
            throw new JobSchedulingException(e.getMessage());
        }

        return jobRequest;
    }

    private Set<Trigger> buildTriggers(JobRequest jobRequest) {
        Trigger trigger = triggerFactory.getTrigger(jobRequest);
        return Set.of(trigger);
    }

    private JobDetail buildJobDetail(JobRequest jobRequest) {
        return jobDetailFactory.getJobDetail(jobRequest);
    }
}
