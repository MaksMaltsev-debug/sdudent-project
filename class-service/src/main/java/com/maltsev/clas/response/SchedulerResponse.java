package com.maltsev.clas.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class SchedulerResponse {
    private String Id;
    private String Subject;
    private Date StartTime;
    private Date FinishTime;
    private String Description;
    private UserResponse creator;
}