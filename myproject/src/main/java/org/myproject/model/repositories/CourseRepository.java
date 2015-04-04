package org.myproject.model.repositories;

 

import java.util.List;

import org.myproject.model.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value="SELECT tbl_COURSE.* "
               + "FROM tbl_COURSE "
               + "WHERE END_EXECUTION_YEAR IS NULL "
               + "ORDER BY CODE", nativeQuery = true)
 public List<Course> findAllPresentCourses();

}