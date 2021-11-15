package service;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import persistence.dto.openedSubjectDTO;

import java.util.List;

public interface openedMapperInter {
    @Select("select * from union.openedsubject")
    public List<openedSubjectDTO> selectAll();

    @Select("SELECT * FROM union.openedsubject where csCode in (select sCode from union.subject where grade = #{grade})")
    public List<openedSubjectDTO> selectGrade(int grade);

    @Select("select * from union.openedsubject where pNumber = #{pNumber}")
    public List<openedSubjectDTO> selectProfessor(String pNumber);

    @Select("SELECT * FROM union.openedsubject where pNumber = #{pNumber} and csCode in (select sCode from union.subject where grade = #{grade})")
    public List<openedSubjectDTO> selectGradeAndProfessor(@Param("grade")int grade, @Param("pNumber")String pNumber);

    @Insert("insert into union.openedsubject values (#{csCode}, #{pNumber}, #{maxStudent}, #{lectureStartTime}, #{lectureEndTime}, #{lectureDay}, #{lectureRoom}, #{periodCourseDescription})")
    public int insertData(openedSubjectDTO openedSubjectDTO);

    @Update("update union.openedsubject set csCode = #{csCode}, pNumber = #{pNumber}, maxStudent = #{maxStudent}, lectureStartTime = #{lectureStartTime}, lectureEndTime = #{lectureEndTime}, lectureDay = #{lectureDay}, lectureRoom = #{lectureRoom}, periodCourseDescription = #{periodCourseDescription} where csCode = #{csCode} ")
    public int updateData(openedSubjectDTO openedSubjectDTO);
}
