package com.auggie.student_server.controller;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin("*")
@RequestMapping("/courseTeacher")
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;

    //教师开设课程（\Teacher\offerCourseList.vue），开设该课程在“本学期”
    @GetMapping("/insert/{cid}/{tid}/{term}")
    public boolean insert(@PathVariable Integer cid, @PathVariable Integer tid, @PathVariable String term) {
        if (courseTeacherService.findBySearch(cid, tid, term).size() != 0) {
//            若已开设该课程，则return false
            return false;
        }
//        若还没有开设该课程，则执行插入该课程的方法
        return courseTeacherService.insertCourseTeacher(cid, tid, term);
    }

    //教师查询当前学期已开设的课程，前端teacher/myOfferCourse.vue中获取到当前学期和教师id传递到后端
    @GetMapping("/findMyCourse/{tid}/{term}")
    public List<Course> findMyCourse(@PathVariable Integer tid, @PathVariable String term) {
        System.out.println("查询教师课程：" + tid + " " + term);
        return courseTeacherService.findMyCourse(tid, term);
    }

    //管理员开课管理中查询已经开设的课程，前端src\views\Admin\selectCourseManage\CourseTacherList.vue侦听器（watch）动态发送请求
    //选课按钮选课的功能在SCTcontroller中
    @PostMapping("/findCourseTeacherInfo")
    public List<CourseTeacherInfo> findCourseTeacherInfo(@RequestBody Map<String, String> map) {
        return courseTeacherService.findCourseTeacherInfo(map);
    }

    //学生页面中选课管理页面中的选课页面，查询本学期已经开设的课程供学生选择，前端src\views\Student\selectCourse\selectCourseList.vue侦听器（watch）动态发送请求
    @PostMapping("/findCourseTeacherInfoWithTerm")
    public List<CourseTeacherInfo> findCourseTeacherInfoWithTerm(@RequestBody Map<String, String> map) {
        return courseTeacherService.findCourseTeacherInfoWithTerm(map);
    }

    //管理员页面开课管理中删除老师已经开设的课程，src\views\Admin\selectCourseManage\CourseTacherList.vue
    @PostMapping("/deleteById")
    public boolean deleteById(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.deleteById(courseTeacher);
    }
}
