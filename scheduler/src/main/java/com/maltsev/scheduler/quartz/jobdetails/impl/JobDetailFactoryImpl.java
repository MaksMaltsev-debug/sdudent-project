package com.maltsev.scheduler.quartz.jobdetails.impl;

import com.maltsev.cross.JobType;
import com.maltsev.cross.request.JobRequest;
import com.maltsev.scheduler.factory.GenericFactory;
import com.maltsev.scheduler.quartz.jobdetails.JobDetailBuilder;
import com.maltsev.scheduler.quartz.jobdetails.JobDetailFactory;
import org.quartz.JobDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobDetailFactoryImpl extends GenericFactory<JobType, JobDetailBuilder> implements JobDetailFactory {

    public JobDetailFactoryImpl(List<JobDetailBuilder> jobDetailBuilders) {
        super(type -> null);
        jobDetailBuilders.forEach(builder -> builder.getSupportedJobTypes()
                .forEach(jobType -> register(jobType, builder)));
    }

    @Override
    public JobDetail getJobDetail(JobRequest jobRequest) {
        return getBuilder(jobRequest.getJobType()).buildJobDetail(jobRequest);
    }

    private JobDetailBuilder getBuilder(JobType jobType) {
        return get(jobType);
    }
}
