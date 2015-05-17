package ar.uba.fi.fiubappREST.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ar.uba.fi.fiubappREST.domain.GroupPicture;
import ar.uba.fi.fiubappREST.representations.GroupCreationRepresentation;
import ar.uba.fi.fiubappREST.representations.GroupRepresentation;

public interface GroupService {

	public GroupRepresentation create(GroupCreationRepresentation groupRepresentation);

	public List<GroupRepresentation> findByProperties(String userName, String name);

	public GroupRepresentation registerStudent(String userName, Integer groupId);

	public List<GroupRepresentation> getStudentGroups(String userName);

	public GroupRepresentation findGroupForStudent(Integer groupId, String userName);

	public GroupPicture getPicture(Integer groupId);

	public void updatePicture(Integer groupId, MultipartFile image, String userName);

}
