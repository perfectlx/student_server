package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StudentCourseTeacherMapper {

    public List<CourseGrade> selectExcellentGrades(Integer sid);

    public List<CourseGrade> selectFailedGrades(Integer sid);

    public void insertScores(List<StudentCourseTeacher> scores);

    public List<CourseTeacherInfo> findByStudentId(@Param("sid") Integer sid,
                                                   @Param("term") String term);

    public List<SCTInfo> findBySearch(@Param("sid") Integer sid,
                                      @Param("sname") String sname,
                                      @Param("sFuzzy") Integer sFuzzy,
                                      @Param("cid") Integer cid,
                                      @Param("cname") String cname,
                                      @Param("cFuzzy") Integer cFuzzy,
                                      @Param("tid") Integer tid,
                                      @Param("tname") String tname,
                                      @Param("tFuzzy") Integer tFuzzy,
                                      @Param("lowBound") Integer lowBound,
                                      @Param("highBound") Integer highBound,
                                      @Param("term") String term);

    @Select("SELECT DISTINCT studentcourseteacher.term FROM studentms.studentcourseteacher studentcourseteacher")
    public List<String> findAllTerm();

    @Select("SELECT * FROM studentms.studentcourseteacher WHERE sid = #{studentcourseteacher.sid} AND cid = #{studentcourseteacher.cid} AND tid = #{studentcourseteacher.tid} AND term = #{studentcourseteacher.term}")
    public List<StudentCourseTeacher> findBySCT(@Param("studentcourseteacher") StudentCourseTeacher studentCourseTeacher);

    @Insert("INSERT INTO studentms.studentcourseteacher (sid, cid, tid, term) VALUES (#{studentcourseteacher.sid}, #{studentcourseteacher.cid}, #{studentcourseteacher.tid}, #{studentcourseteacher.term})")
    public boolean insert(@Param("studentcourseteacher")StudentCourseTeacher studentCourseTeacher);

    @Update("UPDATE studentms.studentcourseteacher SET studentcourseteacher.grade = #{grade} WHERE studentcourseteacher.sid = #{sid} AND studentcourseteacher.tid = #{tid} AND studentcourseteacher.cid = #{cid} AND studentcourseteacher.term = #{term}")
    public boolean updateById(@Param("sid") Integer sid,
                              @Param("cid") Integer cid,
                              @Param("tid") Integer tid,
                              @Param("term") String term,
                              @Param("grade") Integer grade);

    @Delete("DELETE FROM studentms.studentcourseteacher WHERE sid = #{studentcourseteacher.sid} AND tid = #{studentcourseteacher.tid} AND cid = #{studentcourseteacher.cid} AND term = #{studentcourseteacher.term}")
    public boolean deleteBySCT(@Param("studentcourseteacher") StudentCourseTeacher studentcourseteacher);
}
