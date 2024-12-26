package com.hhplus.lecture.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="Registrations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long lectureId;

    @CreatedDate
    private LocalDateTime createdAt; // 강의신청일

    @LastModifiedDate
    private LocalDateTime updatedAt; // 변경일시


    @Builder
    public Registration(Long userId, Long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }

}