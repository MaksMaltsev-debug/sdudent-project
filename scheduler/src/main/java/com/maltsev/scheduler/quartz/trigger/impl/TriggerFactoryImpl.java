package com.maltsev.scheduler.quartz.trigger.impl;


import com.maltsev.cross.JobType;
import com.maltsev.cross.request.JobRequest;
import com.maltsev.scheduler.quartz.trigger.TriggerAssembler;
import com.maltsev.scheduler.quartz.trigger.TriggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TriggerFactoryImpl implements TriggerFactory {

    private static final Map<JobType, TriggerAssembler> TRIGGER_ASSEMBLERS = new EnumMap<>(JobType.class);

    public TriggerFactoryImpl(List<TriggerAssembler> triggerAssemblers) {
        for (TriggerAssembler assembler : triggerAssemblers) {
            TRIGGER_ASSEMBLERS.put(assembler.getSupportedJobType(), assembler);
        }
    }

    @Override
    public Trigger getTrigger(JobRequest jobRequest) {
        return getAssembler(jobRequest.getJobType()).assemble(jobRequest);
    }

    private TriggerAssembler getAssembler(JobType jobType) {
        TriggerAssembler assembler = TRIGGER_ASSEMBLERS.get(jobType);

        if (assembler == null) {
            log.error("Unsupported job type {}", jobType);
            throw new IllegalArgumentException();
        }

        return assembler;
    }
}
