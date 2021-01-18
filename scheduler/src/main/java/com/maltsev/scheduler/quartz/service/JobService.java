package com.maltsev.scheduler.quartz.service;


import com.maltsev.cross.request.JobRequest;

public interface JobService {
    JobRequest createJob(JobRequest jobRequest);

}
