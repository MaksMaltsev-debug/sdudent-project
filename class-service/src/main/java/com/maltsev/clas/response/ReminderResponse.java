package com.maltsev.clas.response;

import com.maltsev.clas.entity.status.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReminderResponse {
    private String id;
    private String description;
    private ZonedDateTime createTime;
    private StatusEnum status;
}
