package com.maltsev.clas.entity;

import com.maltsev.clas.entity.status.ReminderTypes;
import com.maltsev.clas.entity.status.StatusEnum;
import com.maltsev.cross.constatns.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "\"reminders\"")

public class ReminderEntity {
    @Id
    private String id;

    @Column(length = 100)
    private String userId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(length = 100)
    private ZonedDateTime createTime;

    @Column(length = 400)
    private String description;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private ReminderTypes type;

}
