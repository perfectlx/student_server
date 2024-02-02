package com.auggie.student_server.controller;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    //管理员页面课程管理中搜索课程功能，查询所有课程信息（src\views\Admin\courseManage\courseList.vue）
    //教师页面课程设置中开设课程页面查询所有课程信息（src\views\Teacher\offerCourseList.vue）
    @PostMapping("/findBySearch")
    //@RequestBody注解表示map参数应该用请求体中的JSON数据填充。假设请求体中的JSON数据表示一个包含字符串键和字符串值的映射。
    public List<Course> findBySearch(@RequestBody Map<String, String> map) {
        return courseService.findBySearch(map);
    }

    @GetMapping("/findById/{cid}")
    public List<Course> findById(@PathVariable Integer cid) {
        return courseService.findBySearch(cid);
    }

//  管理员课程管理中添加课程功能（src\views\Admin\courseManage\addCourse.vue）
    @PostMapping("/save")
    public boolean save(@RequestBody Course course) {
        System.out.println(course);
        return courseService.insertCourse(course);
    }

    //管理员删除课程（src\views\Admin\courseManage\courseList.vue）
    @GetMapping("/deleteById/{cid}")
    public boolean deleteById(@PathVariable Integer cid) {
        System.out.println("正在删除课程 cid: " + cid);
        return courseService.deleteById(cid);
    }

//    管理员编辑课程（src\views\Admin\courseManage\editorCourse.vue）
    @PostMapping("/updateCourse")
    public boolean updateCourse(@RequestBody Course course) {
        System.out.println("正在修改课程: " + course);
        return courseService.updateById(course);
    }

}
