package com.maltsev.clas.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "\"users\"")
public class UserEntity {
    @Id
    private String id;

    @Column(length = 100)
    private String login;

    @Column(length = 100)
    private String password;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String lastName;
}
