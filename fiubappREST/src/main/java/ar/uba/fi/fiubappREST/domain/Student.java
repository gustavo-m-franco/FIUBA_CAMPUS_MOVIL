package ar.uba.fi.fiubappREST.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ar.uba.fi.fiubappREST.exceptions.CareerAlreadyExistsForStudentException;
import ar.uba.fi.fiubappREST.exceptions.CareerNotFoundForStudentException;
import ar.uba.fi.fiubappREST.exceptions.StudentAlreadyMateException;
import ar.uba.fi.fiubappREST.exceptions.UnableToDeleteTheOnlyCareerForStudentException;
import ar.uba.fi.fiubappREST.utils.CustomDateDeserializer;
import ar.uba.fi.fiubappREST.utils.CustomDateSerializer;

@Entity
@Table(name = "student")
public class Student {

	@Id
	private String userName;
	
	private String password;
	
	private String name;
	
	private String lastName;

	private Boolean isExchangeStudent;
		
	private String passportNumber;
	
	private String fileNumber;
	
	private String email;
	
	private Date dateOfBirth;
	
	private String phoneNumber;
	
	private String currentCity;
	
	private String nationality;
	
	private String comments;
	
	@Enumerated
	private Gender gender;

	@OneToMany(mappedBy="student", cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	private List<StudentCareer> careers;
	
	@OneToOne(mappedBy = "student", cascade={CascadeType.ALL}, orphanRemoval = true)
	private HighSchool highSchool;
	
	@OneToMany(mappedBy="student", cascade={CascadeType.ALL}, orphanRemoval = true)
	private List<Notification> notifications;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
	      name="mates",
	      joinColumns={ @JoinColumn(name="userName", referencedColumnName="userName") },
	      inverseJoinColumns={ @JoinColumn(name="mateUserName", referencedColumnName="userName")})
	private List<Student> mates;
	
	@ManyToMany(mappedBy="members")
	private List<Group> groups;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getIsExchangeStudent() {
		return isExchangeStudent;
	}

	public void setIsExchangeStudent(Boolean isExchangeStudent) {
		this.isExchangeStudent = isExchangeStudent;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<StudentCareer> getCareers() {
		return careers;
	}

	public void setCareers(List<StudentCareer> careers) {
		this.careers = careers;
	}
	
	@JsonIgnore
	public HighSchool getHighSchool() {
		return highSchool;
	}

	public void setHighSchool(HighSchool highSchool) {
		this.highSchool = highSchool;
	}

	@JsonIgnore
	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<Student> getMates() {
		return mates;
	}

	@JsonIgnore
	public void setMates(List<Student> mates) {
		this.mates = mates;
	}

	@JsonIgnore
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void addCareer(final StudentCareer career) {
		if(this.existsCareer(career.getCareer())){
			throw new CareerAlreadyExistsForStudentException(this.userName, career.getCareer().getCode());
		}
		career.setStudent(this);
		this.careers.add(career);
	}
	
	private boolean existsCareer(final Career career){
		StudentCareer foundCareer = (StudentCareer) CollectionUtils.find(this.careers, new Predicate() {
            public boolean evaluate(Object object) {
                return ((StudentCareer) object).getCareer().getCode().equals(career.getCode());
            }
		});
		return foundCareer!=null;
	}

	public void removeCareer(StudentCareer career) {
		StudentCareer studentcareer = findCareer(career);
		this.verifyMoreThanOneCareers();
		this.careers.remove(studentcareer);
		career.setStudent(null);
	}
	
	private void verifyMoreThanOneCareers() {
		if(this.careers.size() < 2){
			throw new UnableToDeleteTheOnlyCareerForStudentException(this.userName);
		}		
	}

	private StudentCareer findCareer(final StudentCareer career) {
		StudentCareer foundCareer = (StudentCareer) CollectionUtils.find(this.careers, new Predicate() {
            public boolean evaluate(Object object) {
                return ((StudentCareer) object).getCareer().getCode().equals(career.getCareer().getCode());
            }
		});
		if(foundCareer==null){
			throw new CareerNotFoundForStudentException(career.getCareer().getCode(), this.userName);
		}
		return foundCareer;
	}
	
    public void addNotification(Notification notification) {
		if(this.notifications==null){
			this.notifications = new ArrayList<Notification>();
		}
		this.notifications.add(notification);		
	}
	
	public void addMate(Student mate){
		if(this.isAlreadyMateWith(mate)){
			throw new StudentAlreadyMateException(this.userName, mate.getUserName());
		}
		this.mates.add(mate);
		mate.getMates().add(this);
	}

	private boolean isAlreadyMateWith(final Student mate) {
		Student foundMate = (Student) CollectionUtils.find(this.mates, new Predicate() {
            public boolean evaluate(Object object) {
                return ((Student) object).getUserName().equals(mate.getUserName());
            }
		});
		return foundMate!=null;
	}

	public Boolean isMateWith(Student student) {
		return this.isAlreadyMateWith(student);
	}	

}
