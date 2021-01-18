package com.maltsev.scheduler.quartz.jobdetails;


import com.maltsev.cross.JobType;
import com.maltsev.cross.request.JobRequest;
import org.quartz.JobDetail;

import java.util.List;

public interface JobDetailBuilder {
    JobDetail buildJobDetail(JobRequest jobRequest);

    List<JobType> getSupportedJobTypes();
}
