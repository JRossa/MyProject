package org.myproject.dao;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Embedded;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.myproject.model.utils.Stamp;

@XmlRootElement(name = "LessonPlanUser")
@XmlAccessorType (javax.xml.bind.annotation.XmlAccessType.FIELD)
public class LessonPlanUser {

	private String username;

	private String title;

	private Long courseId;
 
    private Long teacherId;

    private Long degreeId;

    private String place;
    
    private Integer dayOfWeek;

    private Time timeStartTime;
    
    @XmlSchemaType(name = "date")
    private String startTime;

    private Time timeEndTime;
    
    private String endTime;

    @XmlSchemaType(name = "time")
    private Date startDate;

    private Date endDate;

    private Integer daylight;
    
    private String description;

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

  
    public LessonPlanUser() {
        super();
    }


    
	public LessonPlanUser(String title, String place, String startTime, String endTime) {
		super();
		this.title = title;
		this.place = place;
		this.startTime = startTime;
		this.endTime = endTime;
	}



	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Long getCourseId() {
		return courseId;
	}


	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}


	public Long getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}


	public Long getDegreeId() {
		return degreeId;
	}


	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public Integer getDayOfWeek() {
		return dayOfWeek;
	}


	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}


	public Date getStartDate() {
		return startDate;
	}


	public Time getTimeStartTime() {
		return timeStartTime;
	}


	public void setTimeStartTime(Time startTime) {
		this.timeStartTime = startTime;
	}


	public Time getTimeEndTime() {
		return timeEndTime;
	}


	public void setTimeEndTime(Time endTime) {
		this.timeEndTime = endTime;
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


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

    
}
