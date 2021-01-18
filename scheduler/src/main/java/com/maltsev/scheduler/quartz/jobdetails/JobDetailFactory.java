package com.maltsev.scheduler.quartz.jobdetails;


import com.maltsev.cross.request.JobRequest;
import org.quartz.JobDetail;

public interface JobDetailFactory {
    JobDetail getJobDetail(JobRequest jobRequest);
}
