package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeacherHoursRepository extends JpaRepository<TeacherHours, Long> {

    public List <TeacherHours> findByTeacher (Teacher teacher);
    
    @Query(value="SELECT tbl_TEACHER_HOURS.* FROM tbl_TEACHER_HOURS "
               + "JOIN tbl_EXECUTION_YEAR ON tbl_teacher_hours.EXECUTION_YEAR = tbl_execution_year.ID "
               + "WHERE tbl_execution_year.EXECUTION_YEAR = :executionYear "
               + "AND tbl_teacher_hours.TEACHER_ID = :teacherId", nativeQuery = true)
    public List <TeacherHours> findByTeacherAndExecutionYear (@Param("teacherId") Long teacherId, 
                                                              @Param("executionYear") String executionYear);
}
