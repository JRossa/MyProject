package org.myproject.model.repositories;

import java.util.List;

import org.myproject.model.entities.DegreeCurricularPlansData;
import org.myproject.model.entities.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DegreeCurricularPlansDataRepository extends JpaRepository<DegreeCurricularPlansData, Long> {
	
	
	@Query(value="SELECT tbl_DEGREE_CURRICULAR_PLANS_DATA.* FROM tbl_DEGREE_CURRICULAR_PLANS_DATA "
	           + "WHERE tbl_DEGREE_CURRICULAR_PLANS_DATA.COURSE_CODE = :courseId AND "
	           + "tbl_DEGREE_CURRICULAR_PLANS_DATA.DEGREE_CODE = :degreeId AND "
	           + "tbl_DEGREE_CURRICULAR_PLANS_DATA.END_EXECUTION_YEAR IS NULL ", nativeQuery = true)
	public DegreeCurricularPlansData findCurricularPlanByCourseAndDegree(@Param("courseId") Long courseId,
		                                                	        	 @Param("degreeId") Long degreeId);


}
