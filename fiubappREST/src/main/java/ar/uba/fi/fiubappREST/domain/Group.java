package ar.uba.fi.fiubappREST.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ar.uba.fi.fiubappREST.exceptions.StudentAlreadyMemberOfGroupException;
import ar.uba.fi.fiubappREST.utils.CustomDateDeserializer;
import ar.uba.fi.fiubappREST.utils.CustomDateSerializer;

@Entity
@Table(name = "study_group")
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Date creationDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ownerUserName")
	private Student owner;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
	      name="groups_students",
	      joinColumns={@JoinColumn(name="groupId", referencedColumnName="id")},
	      inverseJoinColumns={@JoinColumn(name="userName", referencedColumnName="userName")})
	private List<Student> members;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Student getOwner() {
		return owner;
	}

	public void setOwner(Student owner) {
		this.owner = owner;
	}

	@JsonIgnore
	public List<Student> getMembers() {
		return members;
	}

	public void setMembers(List<Student> members) {
		this.members = members;
	}

	public void addMember(Student member) {
		if(this.isAlreadyMember(member)){
			throw new StudentAlreadyMemberOfGroupException(member.getUserName(), this.name);
		}
		this.members.add(member);
		member.getGroups().add(this);
	}
	
	private boolean isAlreadyMember(final Student member) {
		Student foundMember = (Student) CollectionUtils.find(this.members, new Predicate() {
            public boolean evaluate(Object object) {
                return ((Student) object).getUserName().equals(member.getUserName());
            }
		});
		return foundMember!=null;
	}
	
}
