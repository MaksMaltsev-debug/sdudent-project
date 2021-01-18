package com.maltsev.scheduler.quartz.trigger;


import com.maltsev.cross.request.JobRequest;
import org.quartz.Trigger;

public interface TriggerFactory {
    Trigger getTrigger(JobRequest jobRequest);
}
