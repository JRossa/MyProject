package org.myproject.model.repositories;


import org.myproject.model.entities.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonPlanRepository extends JpaRepository<LessonPlan, Long> {

}
