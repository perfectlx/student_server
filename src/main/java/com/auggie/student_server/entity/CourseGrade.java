package com.auggie.student_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("CourseGrade")
public class CourseGrade {
    private String courseName;
    private Double grade;
}