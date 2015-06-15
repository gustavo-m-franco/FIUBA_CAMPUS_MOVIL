package ar.uba.fi.fiubappREST.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.uba.fi.fiubappREST.domain.Group;
import ar.uba.fi.fiubappREST.domain.Student;
import ar.uba.fi.fiubappREST.representations.GroupRepresentation;
import ar.uba.fi.fiubappREST.representations.StudentProfileRepresentation;

@Component
public class GroupConverter {
	
	private StudentProfileConverter studentConverter;
	
	@Autowired
	public GroupConverter(StudentProfileConverter studentConverter){
		this.studentConverter = studentConverter;
	}
		
	public GroupRepresentation convert(Student me, Group group){
		GroupRepresentation groupRepresentation = new GroupRepresentation();
		groupRepresentation.setId(group.getId());
		groupRepresentation.setName(group.getName());
		groupRepresentation.setDescription(group.getDescription());
		groupRepresentation.setCreationDate(group.getCreationDate());
		groupRepresentation.setGroupPicture(group.getGroupPictureUrl());
		StudentProfileRepresentation studentRepresentation = this.studentConverter.convert(me, group.getOwner());
		groupRepresentation.setOwner(studentRepresentation);
		groupRepresentation.setAmountOfMembers(group.getMembers().size());
		groupRepresentation.setAmIAMember(me.isMemberOf(group));
		groupRepresentation.setState(group.getState().getName());
		return groupRepresentation;
	}
	
	public GroupRepresentation convert(Group group){
		GroupRepresentation groupRepresentation = new GroupRepresentation();
		groupRepresentation.setId(group.getId());
		groupRepresentation.setName(group.getName());
		groupRepresentation.setDescription(group.getDescription());
		groupRepresentation.setCreationDate(group.getCreationDate());
		groupRepresentation.setGroupPicture(group.getGroupPictureUrl());
		StudentProfileRepresentation studentRepresentation = this.studentConverter.convert(group.getOwner());
		groupRepresentation.setOwner(studentRepresentation);
		groupRepresentation.setAmountOfMembers(group.getMembers().size());
		groupRepresentation.setAmIAMember(false);
		groupRepresentation.setState(group.getState().getName());
		return groupRepresentation;
	}
}
