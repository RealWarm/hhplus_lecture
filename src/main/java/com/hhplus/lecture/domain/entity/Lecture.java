package com.hhplus.lecture.domain.entity;


import com.hhplus.lecture.domain.exception.LectureApplyLimitFullException;
import com.hhplus.lecture.domain.exception.OutOfDateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name="lectures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;               // 강의명
    private String instructor;          // 강사명
    private LocalDateTime openDate;     // 강의 일시
    private Integer maxCapacity;        // 수강 가능인원
    private Integer currentCapacity;    // 현재 수강인원
    @CreatedDate
    private LocalDateTime createdAt;    // 생성일시
    @LastModifiedDate
    private LocalDateTime updatedAt;    // 변경일시


    @Builder
    public Lecture(String title,
                   String instructor,
                   LocalDateTime openDate,
                   Integer maxCapacity,
                   Integer currentCapacity) {
        this.title = title;
        this.instructor = instructor;
        this.openDate = openDate;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }//init


    public Integer addCurrentCapacity() throws LectureApplyLimitFullException {
        if(this.currentCapacity>=this.maxCapacity){
            throw new LectureApplyLimitFullException("공석이 없습니다.");
        }
        return this.currentCapacity++;
    }//addUser


    public Boolean isApplicationPeriod(LocalDateTime now) throws OutOfDateException {
        if(this.openDate.isBefore(now)){
            throw new OutOfDateException("수강 신청 기간이 지났습니다.");
        }//if
        return true;
    }//isApplicationPeriod

}//end