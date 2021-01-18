package com.maltsev.clas.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SetNotificationsRequest {
    @NotEmpty
    private boolean notificationClassStart;

    @NotEmpty
    private boolean activityInClasses;

    @NotEmpty
    private boolean informationAboutUpdates;


}
