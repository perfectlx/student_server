package com.auggie.student_server.controller;

import com.auggie.student_server.entity.CourseGrade;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.entity.SCTInfo;
import com.auggie.student_server.entity.StudentCourseTeacher;
import com.auggie.student_server.service.SCTService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/SCT")
public class SCTcontroller {
    @Autowired
    private SCTService sctService;

    //学生主页查询优秀成绩的科目（成绩大于等于90）
    @GetMapping("/excellent-grades/{sid}")
    public ResponseEntity<List<CourseGrade>> getExcellentGrades(@PathVariable("sid") Integer sid) {
        List<CourseGrade> excellentGrades = sctService.getExcellentGrades(sid);
        return ResponseEntity.ok(excellentGrades);
    }

    //学生主页查询不及格成绩的科目（成绩小于60）
    @GetMapping("/failed-grades/{sid}")
    public ResponseEntity<List<CourseGrade>> getFailedGrades(@PathVariable("sid") Integer sid) {
        List<CourseGrade> failedGrades = sctService.getFailedGrades(sid);
        return ResponseEntity.ok(failedGrades);
    }

    //管理员批量导入学生成绩
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 处理上传的Excel文件，解析成绩数据
            List<StudentCourseTeacher> scores = sctService.parseExcelFile(file);
            //保存成绩到数据库
            sctService.saveScores(scores);
            return ResponseEntity.ok("上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上传失败: " + e.getMessage());
        }
    }

    //学生页面中选课管理页面中的选课功能，点击选课执行改请求
    @PostMapping("/save")
    public String save(@RequestBody StudentCourseTeacher studentCourseTeacher) {
//        判断数据库该学生是否已经选择了此课程
        if (sctService.isSCTExist(studentCourseTeacher)) {
            return "禁止重复选课";
        }
        System.out.println("正在保存 sct 记录：" + studentCourseTeacher);
//        选课记录保存
        return sctService.save(studentCourseTeacher) ? "选课成功" : "选课失败，联系管理员";
    }

    //  学生页面选课管理中查询自己的课表，前端（src\views\Student\selectCourse\querySelectedCourse.vue）通过生命周期函数created()发送请求
//  学生页面查询自己的成绩
    @GetMapping("/findBySid/{sid}/{term}")
    public List<CourseTeacherInfo> findBySid(@PathVariable Integer sid, @PathVariable String term) {
        return sctService.findBySid(sid, term);
    }

    //    查询sct表中的所有学期（不重复）
    @GetMapping("/findAllTerm")
    public List<String> findAllTerm() {
        return sctService.findAllTerm();
    }

    //    管理员删除学生成绩(只会删除当前学期该学生该老师此门课程的成绩，若学生重修过不会删除其他学期的成绩
    @PostMapping("/deleteBySCT")
    public boolean deleteBySCT(@RequestBody StudentCourseTeacher studentCourseTeacher) {
        System.out.println("正在删除 sct 记录：" + studentCourseTeacher);
        return sctService.deleteBySCT(studentCourseTeacher);
    }

    //    管理员查询学生成绩、教师查询学生成绩
    @PostMapping("/findBySearch")
    public List<SCTInfo> findBySearch(@RequestBody Map<String, String> map) {
        return sctService.findBySearch(map);
    }

    //    管理员页面和教师页面修改学生成绩时（编辑成绩）查询到学生该课程信息到页面显示
    @GetMapping("/findById/{sid}/{cid}/{tid}/{term}")
    public SCTInfo findById(@PathVariable Integer sid,
                            @PathVariable Integer cid,
                            @PathVariable Integer tid,
                            @PathVariable String term) {
        return sctService.findByIdWithTerm(sid, cid, tid, term);
    }

    //    管理员页面和教师页面修改学生成绩
    @GetMapping("/updateById/{sid}/{cid}/{tid}/{term}/{grade}")
    public boolean updateById(@PathVariable Integer sid,
                              @PathVariable Integer cid,
                              @PathVariable Integer tid,
                              @PathVariable String term,
                              @PathVariable Integer grade) {
        return sctService.updateById(sid, cid, tid, term, grade);
    }

    //    管理员页面删除学生成绩
    @GetMapping("/deleteById/{sid}/{cid}/{tid}/{term}")
    public boolean deleteById(@PathVariable Integer sid,
                              @PathVariable Integer cid,
                              @PathVariable Integer tid,
                              @PathVariable String term) {
        return sctService.deleteById(sid, cid, tid, term);
    }



}
