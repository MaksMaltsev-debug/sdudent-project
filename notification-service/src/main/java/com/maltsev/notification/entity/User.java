package com.maltsev.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    private String id;
    private String email;
}
