package org.myproject.model.repositories;


import java.util.List;

import org.myproject.model.entities.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonPlanRepository extends JpaRepository<LessonPlan, Long> {

	
	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.TEACHER_ID = :teacherId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByTeacher(@Param("teacherId") Long teacherId);
	
	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.COURSE_ID = :courseId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByCourse(@Param("courseId") Long courseId);
	
	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.DEGREE_ID = :degreeId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByDegree(@Param("degreeId") Long degreeId);

	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.TEACHER_ID = :teacherId AND "
	           + "tbl_LESSON_PLAN.COURSE_ID = :courseId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByTeacherAndCourse(@Param("teacherId") Long teacherId,
		                                                	  @Param("courseId") Long courseId);

	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.TEACHER_ID = :teacherId AND "
	           + "tbl_LESSON_PLAN.DEGREE_ID = :degreeId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByTeacherAndDegree(@Param("teacherId") Long teacherId,
		                                                	  @Param("degreeId") Long degreeId);

	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.COURSE_ID = :courseId AND "
	           + "tbl_LESSON_PLAN.DEGREE_ID = :degreeId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByCourseAndDegree(@Param("courseId") Long courseId,
		                                                	  @Param("degreeId") Long degreeId);


	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
			   + "WHERE tbl_LESSON_PLAN.TEACHER_ID = :teacherId AND "
	           + "tbl_LESSON_PLAN.COURSE_ID = :courseId AND "
	           + "tbl_LESSON_PLAN.DEGREE_ID = :degreeId  ", nativeQuery = true)
	public List <LessonPlan> findLessonPlanByTeacherAndCourseAndDegree(@Param("teacherId") Long teacherId,
			                                                           @Param("courseId") Long courseId,
		                                                	           @Param("degreeId") Long degreeId);

	
	@Query(value="SELECT tbl_LESSON_PLAN.* FROM tbl_LESSON_PLAN "
	           + "WHERE tbl_LESSON_PLAN.DESCRIPTION IS NULL ", nativeQuery = true)
	public List <LessonPlan> findAllMissing();
	

}
