package org.myproject.support.survey;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;






import org.myproject.report.AbstractBaseReportBean.ExportOption;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Named(value = "surveyMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SurveyMBean {

    public enum ExportOption {FILE, PDF, HTML, EXCEL, CSV, XLSX, RTF}

    private ExportOption exportOption;
     
    private String[] questions = {"What is your age?","Where do you come from?","Are you married?"};
    private String answer;
    private String question;
    private int currentQuestion = 0;
    private boolean over  = false;
    private boolean last = false;

    
    public SurveyMBean () {
        this.currentQuestion = 0;
        this.over  = false;
        this.last = false;
    }
    
    @PostConstruct
    void init () {
        // TODO - inicializar os valores para o selectOneRadio
    }
    
    public String getAnswer() {
        System.out.println("Get Answer");
        
        return this.answer;
    }

    public void setAnswer(String answer) {       
        System.out.println("Set Answer");
        
        if(this.currentQuestion < this.questions.length) {
            System.out.println("To the question  \"" + questions[currentQuestion] + "\" your answer is " + answer);
            
            this.currentQuestion++;
            if (this.currentQuestion == (this.questions.length - 1)) {
                this.last = true;
            }
            if (this.currentQuestion == this.questions.length) {
                this.over = true;
            }
        }
    }    

    public String getQuestion() {
        System.out.println("Get Question");
        
        if (!this.over) {
            return this.questions[this.currentQuestion];
        }
        else {
            return "";
        }
    }

    public void setQuestion(String question) {
        System.out.println("Set Question");
        
        this.question = question;
    }

    public void next() {
        System.out.println("Next");
    }

    public String reset() {
        System.out.println("Reset");
        
        this.currentQuestion = 0;
        this.over = false;
        this.last = false;
        
        return "";
        //return "/pages/survey/questionTeacher.xhtml";
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
    
    public ExportOption getExportOption() {
        return exportOption;
      }

    public void setExportOption(ExportOption exportOption) {
       this.exportOption = exportOption;
    }

}