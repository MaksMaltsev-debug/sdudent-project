package com.maltsev.cross.request;

import com.maltsev.cross.JobType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@ToString
@Builder
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    @NotNull(message = "Job type not defined")
    private JobType jobType;
    private String userId;
    private String classId;
    private Date startDate;
}
