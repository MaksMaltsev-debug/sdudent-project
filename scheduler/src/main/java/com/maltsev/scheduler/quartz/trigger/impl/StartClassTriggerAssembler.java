package com.maltsev.scheduler.quartz.trigger.impl;


import com.maltsev.cross.JobType;
import com.maltsev.cross.request.JobRequest;
import com.maltsev.scheduler.quartz.trigger.TriggerAssembler;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Component
public class StartClassTriggerAssembler implements TriggerAssembler {

    @Override
    public Trigger assemble(JobRequest request) {
        log.info(String.format("StartClassTriggerAssembler assemble request %s start date %s", request, getFormattedString(request)));
        TriggerKey triggerKey = TriggerKey.triggerKey(request.getClassId(), request.getJobType().name());
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withDescription("Start Class Event")
                .startAt(getStartAt(request))
                .build();
    }

    @Override
    public JobType getSupportedJobType() {
        return JobType.START_CLASS;
    }

    private Date getStartAt(JobRequest jobRequest) {
        return jobRequest.getStartDate();
    }

    private String getFormattedString(JobRequest request) {
        ZonedDateTime zonedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(request.getStartDate().getTime()), ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(zonedTime);
    }
}