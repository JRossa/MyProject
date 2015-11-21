package org.myproject.model.repositories;



import java.util.Date;
import java.util.List;

import org.myproject.model.entities.TeacherLessonPlanHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeacherLessonPlanHoursRepository extends JpaRepository<TeacherLessonPlanHours, Long> {


	@Query(value="SELECT TEACHER_ID, COURSE_ID, ROUND(SUM(HOURS)) AS TOT_HOURS FROM tbl_TEACHER_LESSON_PLAN_HOURS "
	           + "WHERE START_DATE >= :startDate AND "
	           + "END_DATE <= :endDate "
	           + "GROUP BY TEACHER_ID, COURSE_ID ", nativeQuery = true)
	public List <Object[]> findTeacherCoursesHoursBetweenStartDateAndEndDate(@Param("startDate") Date startDate,
	                                                                         @Param("endDate") Date endDate);

	@Query(value="SELECT TEACHER_ID, ROUND(SUM(HOURS)) AS TOT_HOURS FROM tbl_TEACHER_LESSON_PLAN_HOURS "
	           + "WHERE START_DATE >= :startDate AND "
	           + "END_DATE <= :endDate "
	           + "GROUP BY TEACHER_ID ", nativeQuery = true)
	public List <Object[]> findTeacherHoursBetweenStartDateAndEndDate(@Param("startDate") Date startDate,
	                                                                  @Param("endDate") Date endDate);

	@Query(value="SELECT TEACHER_ID, COURSE_ID, ROUND(SUM(HOURS)) AS TOT_HOURS FROM tbl_TEACHER_LESSON_PLAN_HOURS "
	           + "WHERE TEACHER_ID = :teacherId AND "
	           + "START_DATE >= :startDate AND "
	           + "END_DATE <= :endDate "
	           + "GROUP BY TEACHER_ID ", nativeQuery = true)
	public List <Object[]> findTeacherCoursesHoursByTeacherBetweenStartDateAndEndDate(@Param("teacherId") Long teacherId,
			                                                                          @Param("startDate") Date startDate,
	                                                                                  @Param("endDate") Date endDate);


}   
