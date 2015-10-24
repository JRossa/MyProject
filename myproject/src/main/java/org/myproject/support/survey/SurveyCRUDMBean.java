package org.myproject.support.survey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyTeacher;
import org.myproject.model.entities.SurveyTeacherAnswer;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.myproject.model.entities.TeacherHoursExecutionYear;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyQuestionRepository;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.repositories.SurveyTeacherAnswerRepository;
import org.myproject.model.repositories.SurveyTeacherRepository;
import org.myproject.model.repositories.TeacherHoursExecutionYearRepository;
import org.myproject.model.repositories.TeacherHoursRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.Cryptor;
import org.myproject.support.survey.SurveyMBean.SurveyOption;
import org.myproject.support.teacher.TeacherMBean;
import org.myproject.support.teacherhours.TeacherHoursExecutionYearMBean;
import org.myproject.support.teacherhours.TeacherHoursMBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Named(value = "surveyCRUDMBean")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SurveyCRUDMBean extends BaseBean {


	private static final long serialVersionUID = -6938073535629977704L;

    @Inject
    private CourseRepository courseRepository;

    @Inject
    private SurveyRepository surveyRepository;

    @Inject
    private SurveyQuestionRepository surveyQuestionRepository;

    @Inject
    private SurveyAnswerScaleRepository surveyAnswerScaleRepository;

    @Inject
    private SurveyTeacherRepository surveyTeacherRepository;

    @Inject
    private SurveyTeacherAnswerRepository surveyTeacherAnswerRepository;

    @Inject
    private TeacherHoursRepository teacherHoursRepository;

    @Inject
    private TeacherHoursExecutionYearRepository teacherHoursExecutionYearRepository;

    @Inject
	private SurveyMBean mbSurveyMBean;
 
	@Inject
	private TeacherMBean mbTeacherMBean;

	@Inject
	private TeacherHoursMBean mbTeacherHoursMBean;

	@Inject
	private TeacherHoursExecutionYearMBean mbTeacherHoursExecutionYearMBean;

	
	public SurveyCRUDMBean () {
    	

    }
 
    

	private void saveSurveyTeacher(Teacher teacher, Course course, Date date) {
	
		Survey survey = this.mbSurveyMBean.getActiveSurvey();
		
		SurveyTeacher surveyTeacher = new SurveyTeacher();
		
		surveyTeacher.setSurvey(survey);
		surveyTeacher.setTeacher(teacher);
		surveyTeacher.setCourse(course);
		surveyTeacher.setDate(date);
		
		this.surveyTeacherRepository.saveAndFlush(surveyTeacher);
	}
	
	
	private void saveTeacherAnswer (Teacher teacher, Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		System.out.println("Date    : " + sdf.format(date));
    	String stamp;
    	
    	
     	
		try {
			stamp = Cryptor.EncryptString(sdf.format(date));

	    	System.out.println("----------------- Survey N : " + this.mbSurveyMBean.getSurveyAnswer().size());
	    	

	    	for (SurveyAnswer sa: this.mbSurveyMBean.getSurveyAnswer()) {
	    		System.out.println("Survey          : " + sa.getSurvey());
	    		System.out.println("Survey Question : " + sa.getQuestion());
	    		System.out.println("Survey Answer   : " + sa.getValue());
	    		System.out.println("Survey Course   : " + sa.getCourse());
	    		System.out.println("Stamp           : " + stamp);

		       	SurveyTeacherAnswer surveyTeacherAnswer = new SurveyTeacherAnswer();
		    	surveyTeacherAnswer.setStamp(stamp);

	    		Survey survey = this.surveyRepository.findOne(sa.getSurvey());
	        	surveyTeacherAnswer.setSurvey(survey);;
	        	
	        	SurveyQuestion question = this.surveyQuestionRepository.findOne(sa.getQuestion());
	        	surveyTeacherAnswer.setQuestion(question);
	        	
	        	SurveyAnswerScale answer = this.surveyAnswerScaleRepository.findOne(sa.getAnswer());
	        	surveyTeacherAnswer.setAnswer(answer);
	        		
	        	surveyTeacherAnswer.setValue(sa.getValue());
	        	
	        	if (sa.getCourse() != null) {
	        		Course course = this.courseRepository.findOne(sa.getCourse());
	        		surveyTeacherAnswer.setCourse(course);
	        	} else {
	        		surveyTeacherAnswer.setCourse(null);
	        	}

	        	this.surveyTeacherAnswerRepository.saveAndFlush(surveyTeacherAnswer);
	     
	    	}

	    	
 		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

    public String computeExecutionYear (Date date) {
        Integer executionYear = null;
 
 //       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        
        SimpleDateFormat yearDate = new SimpleDateFormat("yyyy");
        SimpleDateFormat mounthDate = new SimpleDateFormat("MM");

        Integer intMounth = Integer.parseInt(mounthDate.format(date).toString());
        Integer intYear = Integer.parseInt(yearDate.format(date).toString());

        if (intMounth >= 10) {
        	executionYear = intYear;
        } else {
        	executionYear = (intYear-1);
        }

        return executionYear.toString();
    }

    
	public void saveAnswers ()  {
    	Date date = new Date();
   	
    	this.mbSurveyMBean.getSurveyOption();
    	
		if (this.mbSurveyMBean.getSurveyOption().equals(SurveyOption.TEACHER)) {
    		Teacher teacher = this.mbTeacherMBean.getSelectedTeacher();
    		
    		this.saveTeacherAnswer(teacher, date);
    		this.saveSurveyTeacher(teacher, null, date);
    		
    		String executionYear = this.computeExecutionYear(date);
    		
    		List<TeacherHoursExecutionYear> teacherHoursExecutionYear = 
    				this.teacherHoursExecutionYearRepository.findByTeacherAndExecutionYear(teacher.getId(), executionYear);

    		for (TeacherHoursExecutionYear teacherHoursExecYear: teacherHoursExecutionYear) {
    			teacherHoursExecYear.setSurveyDone(true);
    			this.teacherHoursExecutionYearRepository.saveAndFlush(teacherHoursExecYear);
    		}
		}
    	
		
		if (this.mbSurveyMBean.getSurveyOption().equals(SurveyOption.TEACHERHOURS)) {
    		TeacherHours teacherHours = this.mbTeacherHoursMBean.getSelectedTeacherHours();
    		
    		
    		this.saveTeacherAnswer(teacherHours.getTeacher(), date);
    		this.saveSurveyTeacher(teacherHours.getTeacher(), teacherHours.getCourse(), date);
    		
    		teacherHours.setSurveyDone(true);
    		
    		this.teacherHoursRepository.saveAndFlush(teacherHours);
    	}
		
		if (this.mbSurveyMBean.getSurveyOption().equals(SurveyOption.TEACHERHOURSEXECUTIONYEAR)) {
    		TeacherHoursExecutionYear teacherHoursExecutionYear = 
    				this.mbTeacherHoursExecutionYearMBean.getSelectedTeacherHoursExecutionYear();
    		
    		
    		this.saveTeacherAnswer(teacherHoursExecutionYear.getTeacher(), date);
    		this.saveSurveyTeacher(teacherHoursExecutionYear.getTeacher(), null, date);

    		teacherHoursExecutionYear.setSurveyDone(true);
    		
    		this.teacherHoursExecutionYearRepository.saveAndFlush(teacherHoursExecutionYear);
		
		}
 		
		

	}
	
	
	public void save () {
    	this.mbSurveyMBean.reset();
    	
		this.saveAnswers();
    	
    }
    
    
    public void sendEMail () {
    	
    }
    
    

}