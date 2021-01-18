package com.maltsev.clas.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table
@Getter
public class Image {
    @Id
    private String userId;

    @Lob
    private byte[] image;
}
