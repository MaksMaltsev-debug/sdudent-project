package com.maltsev.scheduler.quartz.job;

import com.maltsev.scheduler.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.maltsev.cross.constatns.SchedulerConstants.CLASS_ID_KEY;
import static com.maltsev.cross.constatns.SchedulerConstants.USER_ID_KEY;

@Slf4j
@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EndClassEventJob implements Job {
    private ClassService classService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        String userId = jobDataMap.getString(USER_ID_KEY);
        String classId = jobDataMap.getString(CLASS_ID_KEY);

        log.info("Executing quartz job user {} class {}", userId, classId);
        classService.endClass(userId, classId);
    }

    @Autowired
    public void setClassService(ClassService classService) {
        this.classService = classService;
    }
}
