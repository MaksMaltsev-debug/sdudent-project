package com.maltsev.scheduler.quartz.jobdetails.impl;

import com.maltsev.cross.JobType;
import com.maltsev.cross.request.JobRequest;
import com.maltsev.scheduler.quartz.job.StartClassEventJob;
import com.maltsev.scheduler.quartz.jobdetails.JobDetailBuilder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.maltsev.cross.JobType.START_CLASS;
import static com.maltsev.cross.constatns.SchedulerConstants.CLASS_ID_KEY;
import static com.maltsev.cross.constatns.SchedulerConstants.USER_ID_KEY;


@Slf4j
@Component
public class StartClassJobDetailBuilder implements JobDetailBuilder {

    @Override
    public JobDetail buildJobDetail(JobRequest jobRequest) {
        log.info(String.format("StartClassJobDetailBuilder buildJobDetail %s", jobRequest));
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(USER_ID_KEY, jobRequest.getUserId());
        jobDataMap.put(CLASS_ID_KEY, jobRequest.getClassId());

        return JobBuilder.newJob(StartClassEventJob.class)
                .setJobData(jobDataMap)
                .withIdentity(jobRequest.getClassId(), jobRequest.getJobType().name())
                .requestRecovery()
                .storeDurably()
                .build();
    }

    @Override
    public List<JobType> getSupportedJobTypes() {
        return Collections.singletonList(START_CLASS);
    }
}
