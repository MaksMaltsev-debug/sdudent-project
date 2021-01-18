package com.maltsev.clas.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
@Getter
public class NotificationsResponse {
    private boolean notificationClassStart;
    private boolean activityInClasses;
    private boolean informationAboutUpdates;
}
