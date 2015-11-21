package org.myproject.model.entities;

import org.myproject.model.utils.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

//@Table(name = "tbl_TEACHER", uniqueConstraints = @UniqueConstraint(columnNames = { "FULL_NAME" /*
//                                                                                                * ,
//                                                                                                * "COURSE_CODE"
//                                                                                                * ,
//                                                                                                * "DEGREE_CODE"
//                                                                                                * ,
//                                                                                                * "EXECUTION_YEAR"
//                                                                                                * ,
//                                                                                                * "SEMESTER"
//                                                                                                */}))

// VER - Não funciona só com t, é preciso ter pelo menos 2 itens

@NamedQueries(value = {
		@NamedQuery(name = "Teacher.findAllTeachers", query = "select t.fullName, t.idNumber from Teacher t"),
		@NamedQuery(name = "Teacher.findCatNameTeachers", query = "select t.category, t.fullName from Teacher t"),
		@NamedQuery(name = "Teacher.CategoryName", query = "select t.category, t.fullName, t.contract, t.id  from Teacher t") })



@Entity
@Table(name = "tbl_TEACHER")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class Teacher extends BaseEntity<Long> {

    private static final long serialVersionUID = -7982274754762694904L;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("COURSE_CODE ASC")
    private List<Professorship> professorship = new LinkedList<Professorship>();

    public List<Professorship> getProfessorship() {
        return professorship;
    }

    public void setProfessorship(List<Professorship> professorship) {
        this.professorship = professorship;
    }

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "MILITARY_SITUATION")
    private Integer militarySituation;

    @Column(name = "ID_NUMBER")
    private String idNumber;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "CONTRACT")
    private String contract;

    @Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_DATE", length = 10)
    private Date beginDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 10)
    private Date endDate;

    @Column(name = "E_MAIL")
    private String EMail;

    @Column(name = "MOBILE_PHONE")
    private String mobilePhone;

    @Column(name = "SCIENTIFIC_FIELD")
    private String scientificField;

    @Column(name = "ACADEMIC_DEGREE")
    private String academicDegree;

    @Temporal(TemporalType.DATE)
    @Column(name = "CATEGORY_DATE", length = 10)
    private Date categoryDate;

    @Column(name = "IN_CHARGE")
    private Boolean inCharge;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_GROUP_ID")
    private CategoryGroup categoryGroup;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MASTER_DEGREE_ID")
    private MasterDegreeType masterDegree;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCIENTIFIC_FIELD_ID")
    private ScientificField scientificFieldId;
   
    
    public Teacher() {
        
    }

    public Teacher(String category, String idNumber, String fullName) {
        super();
        this.category = category;
        this.idNumber = idNumber;
        this.fullName = fullName;
    }

    public Teacher(Long id, String category, String id_number, String full_name) {
        super();
        this.setId(id);
        this.category = category;
        this.idNumber = id_number;
        this.fullName = full_name;
    }

 
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getMilitarySituation() {
        return militarySituation;
    }

    public void setMilitarySituation(Integer militarySituation) {
        this.militarySituation = militarySituation;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String eMail) {
        EMail = eMail;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getScientificField() {
        return scientificField;
    }

    public void setScientificField(String scientificField) {
        this.scientificField = scientificField;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public Date getCategoryDate() {
        return categoryDate;
    }

    public void setCategoryDate(Date categoryDate) {
        this.categoryDate = categoryDate;
    }

    public Boolean getInCharge() {
        return inCharge;
    }

    public void setInCharge(Boolean inCharge) {
        this.inCharge = inCharge;
    }

    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

	public MasterDegreeType getMasterDegree() {
		return masterDegree;
	}

	public void setMasterDegree(MasterDegreeType masterDegree) {
		this.masterDegree = masterDegree;
	}

	public ScientificField getScientificFieldId() {
		return scientificFieldId;
	}

	public void setScientificFieldId(ScientificField scientificFieldId) {
		this.scientificFieldId = scientificFieldId;
	}

    
}
