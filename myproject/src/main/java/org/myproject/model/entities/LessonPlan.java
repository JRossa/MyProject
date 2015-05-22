package org.myproject.model.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.myproject.model.utils.BaseEntity;
import org.myproject.model.utils.Stamp;

@Entity
@Table(name = "tbl_LESSON_PLAN")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class LessonPlan extends BaseEntity<Long>{

    private static final long serialVersionUID = 155500299211209388L;

    @Column(name = "TITLE")
    private String title;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_ID")
    private Course course;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEGREE_ID")
    private Degree degree;

    @Column(name = "PLACE")
    private String place;
    
//    Only store date    
//    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

//    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DAYLIGHT")
    private Integer daylight;
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOCKED")
    private Boolean locked;

    @Embedded
    private Stamp stamp;
    
//    @Column(name = "CREATION_USER")
//    private String creationUser;
//
//    @Column(name = "CREATION_DATE")
//    private Date creationDate;
//
//    @Column(name = "MODIFICATION_USER")
//    private String modificationUser;
//
//    @Column(name = "MODIFICATION_DATE")
//    private Date modificationDate;

    
    public LessonPlan() {
        super();
    }


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Course getCourse() {
		return course;
	}


	public void setCourse(Course course) {
		this.course = course;
	}


	public Teacher getTeacher() {
		return teacher;
	}


	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}


	public Degree getDegree() {
		return degree;
	}


	public void setDegree(Degree degree) {
		this.degree = degree;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Integer getDaylight() {
		return daylight;
	}


	public void setDaylight(Integer daylight) {
		this.daylight = daylight;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Boolean getLocked() {
		return locked;
	}


	public void setLocked(Boolean locked) {
		this.locked = locked;
	}


//	public String getCreationUser() {
//		return creationUser;
//	}
//
//
//	public void setCreationUser(String creationUser) {
//		this.creationUser = creationUser;
//	}
//
//
//	public Date getCreationDate() {
//		return creationDate;
//	}
//
//
//	public void setCreationDate(Date creationDate) {
//		this.creationDate = creationDate;
//	}
//
//
//	public String getModificationUser() {
//		return modificationUser;
//	}
//
//
//	public void setModificationUser(String modificationUser) {
//		this.modificationUser = modificationUser;
//	}
//
//
//	public Date getModificationDate() {
//		return modificationDate;
//	}
//
//
//	public void setModificationDate(Date modificationDate) {
//		this.modificationDate = modificationDate;
//	}
//

	public Stamp getStamp() {
		return stamp;
	}


	public void setStamp(Stamp stamp) {
		this.stamp = stamp;
	}

    
}
