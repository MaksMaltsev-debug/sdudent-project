package com.maltsev.clas.entity;

import com.maltsev.clas.entity.status.StatusEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static com.maltsev.clas.constants.URIConstants.CREATOR;
import static com.maltsev.clas.constants.URIConstants.SUBSCRIBERS;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Table(name = "\"users\"")
public class UserEntity {
    @Id
    private String id;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(length = 100)
    private String email;

    @OneToMany(mappedBy = CREATOR, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<ClassEntity> userClasses;

    @ManyToMany(mappedBy = SUBSCRIBERS, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<ClassEntity> subscribedClasses;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(length = 10)
    private boolean notificationClassStart;

    @Column(length = 10)
    private boolean activityInClasses;

    @Column(length = 10)
    private boolean informationAboutUpdates;

    @Column(length = 100)
    private String description;
}
