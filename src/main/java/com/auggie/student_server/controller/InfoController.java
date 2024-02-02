package com.auggie.student_server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@CrossOrigin("*")
public class InfoController {

    private final String CURRENT_TERM = "6";
    private final boolean FORBID_COURSE_SELECTION = false;

    @RequestMapping("/getCurrentTerm")
    //当前学期数值
    public String getCurrentTerm() {
        return CURRENT_TERM;
    }

    @RequestMapping("/getForbidCourseSelection")
    public boolean getForbidCourseSelection() {
        return FORBID_COURSE_SELECTION;
    }
}
