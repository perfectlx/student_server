package com.auggie.student_server.service;

import com.alibaba.excel.context.AnalysisContext;
import com.auggie.student_server.entity.CourseGrade;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.entity.SCTInfo;
import com.auggie.student_server.entity.StudentCourseTeacher;
import com.auggie.student_server.mapper.StudentCourseTeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SCTService {
    @Autowired
    private StudentCourseTeacherMapper studentCourseTeacherMapper;

    public List<CourseGrade> getExcellentGrades(Integer sid) {
        return studentCourseTeacherMapper.selectExcellentGrades(sid);
    }

    public List<CourseGrade> getFailedGrades(Integer sid) {
        return studentCourseTeacherMapper.selectFailedGrades(sid);
    }

    public void saveScores(List<StudentCourseTeacher> scores) {
        studentCourseTeacherMapper.insertScores(scores);
    }

    //EasyExcel.read 方法读取输入流，ReadListener监听 Excel 数据的解析过程。
    //invoke 方法处理每一行的数据，并将其添加到 StudentCourseTeacher 列表中
    public List<StudentCourseTeacher> parseExcelFile(MultipartFile file) throws IOException {
        List<StudentCourseTeacher> scores = new ArrayList<>();
        //将上传的文件MultipartFile对象 file 转换为一个InputStream输入流，以便后续可以使用输入流来读取文件的内容
        //file.getInputStream() 打开从前端上传的 Excel 文件的输入流
        try (InputStream inputStream = file.getInputStream()) {
//            使用 EasyExcel 的 read 方法来读取 Excel 文件，指定 StudentCourseTeacher.class 作为目标对象的类型
            EasyExcel.read(inputStream, StudentCourseTeacher.class, new ReadListener<StudentCourseTeacher>() {
                @Override
                public void onException(Exception exception, AnalysisContext context) throws Exception {
                    // 处理异常
                }

                @Override
                public void invoke(StudentCourseTeacher score, AnalysisContext context) {
                    //处理每一行数据，score 对象包含了一行的数据，将该行数据添加到 StudentCourseTeacher 列表中
                    //默认第一列和第一行数据不读取
                    scores.add(score);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 解析完成后的操作
                }
            }).sheet().doRead();
        }
//返回解析后的StudentCourseTeacher对象
        return scores;
    }

    public List<CourseTeacherInfo> findBySid(Integer sid, String term) {
        return studentCourseTeacherMapper.findByStudentId(sid, term);
    }

    public List<String> findAllTerm() {
        return studentCourseTeacherMapper.findAllTerm();
    }

    public boolean isSCTExist(StudentCourseTeacher studentCourseTeacher) {
        return studentCourseTeacherMapper.findBySCT(studentCourseTeacher).size() != 0;
    }

    public boolean save(StudentCourseTeacher studentCourseTeacher) {
        return studentCourseTeacherMapper.insert(studentCourseTeacher);
    }

    public boolean deleteBySCT(StudentCourseTeacher studentCourseTeacher) {
        return studentCourseTeacherMapper.deleteBySCT(studentCourseTeacher);
    }

    public boolean deleteById(Integer sid, Integer cid, Integer tid, String  term) {
        StudentCourseTeacher studentCourseTeacher = new StudentCourseTeacher();
        studentCourseTeacher.setSid(sid);
        studentCourseTeacher.setCid(cid);
        studentCourseTeacher.setTid(tid);
        studentCourseTeacher.setTerm(term);
        return studentCourseTeacherMapper.deleteBySCT(studentCourseTeacher);
    }

    public SCTInfo findByIdWithTerm(Integer sid, Integer cid, Integer tid, String term) {
        return studentCourseTeacherMapper.findBySearch(
                sid, null, 0,
                cid, null, 0,
                tid, null, 0,
                null, null, term).get(0);
    }

    public boolean updateById(Integer sid, Integer cid, Integer tid, String term, Integer grade) {
        return studentCourseTeacherMapper.updateById(sid, cid, tid, term, grade);
    }

    public List<SCTInfo> findBySearch(Map<String, String> map) {
        Integer sid = null, cid = null, tid = null;
        String sname = null, cname = null, tname = null, term = null;
        Integer sFuzzy = null, cFuzzy = null, tFuzzy = null;
        Integer lowBound = null, highBound = null;

        if (map.containsKey("cid")) {
            try {
                cid = Integer.parseInt(map.get("cid"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("sid")) {
            try {
                sid = Integer.parseInt(map.get("sid"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("tid")) {
            try {
                tid = Integer.parseInt(map.get("tid"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("sname")) {
            sname = map.get("sname");
        }
        if (map.containsKey("tname")) {
            tname = map.get("tname");
        }
        if (map.containsKey("cname")) {
            cname = map.get("cname");
        }
        if (map.containsKey("term")) {
            term = map.get("term");
        }
        if (map.containsKey("sFuzzy")) {
            sFuzzy = map.get("sFuzzy").equals("true") ? 1 : 0;
        }
        if (map.containsKey("tFuzzy")) {
            tFuzzy = map.get("tFuzzy").equals("true") ? 1 : 0;
        }
        if (map.containsKey("cFuzzy")) {
            cFuzzy = map.get("cFuzzy").equals("true") ? 1 : 0;
        }
        if (map.containsKey("lowBound")) {
            try {
                lowBound = Integer.parseInt(map.get("lowBound"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("highBound")) {
            try {
                highBound = Integer.valueOf(map.get("highBound"));
            }
            catch (Exception e) {
            }
        }

        System.out.println("SCT 查询：" + map);
        return studentCourseTeacherMapper.findBySearch(
                sid, sname, sFuzzy,
                cid, cname, cFuzzy,
                tid, tname, tFuzzy,
                lowBound, highBound, term);
    }
}
