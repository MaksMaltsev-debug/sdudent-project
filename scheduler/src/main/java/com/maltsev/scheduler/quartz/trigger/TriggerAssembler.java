package com.maltsev.scheduler.quartz.trigger;


import com.maltsev.cross.JobType;
import com.maltsev.cross.request.JobRequest;
import org.quartz.Trigger;

public interface TriggerAssembler {
    Trigger assemble(JobRequest jobRequest);

    JobType getSupportedJobType();
}
