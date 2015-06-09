package ar.uba.fi.fiubappREST.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import ar.uba.fi.fiubappREST.domain.Discussion;
import ar.uba.fi.fiubappREST.domain.DiscussionMessage;
import ar.uba.fi.fiubappREST.domain.DiscussionReportInformation;
import ar.uba.fi.fiubappREST.domain.Group;
import ar.uba.fi.fiubappREST.persistance.DiscussionRepository;
import ar.uba.fi.fiubappREST.persistance.GroupRepository;

@Service
public class ReportServiceImpl implements ReportService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	private GroupRepository groupRepository;
	private DiscussionRepository discussionRepository;
	
	@Value("classpath:defaultGroupPicture.png")
	private Resource defaultGroupPicture;
		
	@Autowired
	public ReportServiceImpl(GroupRepository groupRepository, DiscussionRepository discussionRepository){
		this.groupRepository = groupRepository;
		this.discussionRepository = discussionRepository;
	}
	
	@Override
	public List<DiscussionReportInformation> getMostActiveDiscussions(Date dateFrom, Date dateTo, Integer values) {
		LOGGER.info(String.format("Creating %s registers for discussions report from %tm/%td/%ty to %tm/%td/%ty.", values, dateFrom, dateFrom, dateFrom, dateTo, dateTo, dateTo));
		List<Discussion> discussions = (List<Discussion>) this.discussionRepository.findAll();
		List<Discussion> filteredDiscussions = getFilteredDiscussions(dateFrom,	dateTo, values, discussions);
		List<DiscussionReportInformation> discussionReporInformations = buildReportInformation(filteredDiscussions);
		LOGGER.info(String.format("%s registers were created for discussions report from %tm/%td/%ty to %tm/%td/%ty.", values, dateFrom, dateFrom, dateFrom, dateTo, dateTo, dateTo));
		return discussionReporInformations;
	}

	private List<DiscussionReportInformation> buildReportInformation(List<Discussion> filteredDiscussions) {
		List<DiscussionReportInformation> discussionReporInformations = new ArrayList<DiscussionReportInformation>();
		for (Discussion discussion : filteredDiscussions) {
			DiscussionReportInformation information = new DiscussionReportInformation();
			information.setDiscussionName(discussion.getDiscussionName());
			information.setAmountOfComments(discussion.getMessages().size());
			Group group = this.groupRepository.findGroupForDiscussion(discussion.getId());
			information.setGroupName(group.getName());
			information.setAmountOfGroupMembers(group.getMembers().size());
			discussionReporInformations.add(information);
		}
		return discussionReporInformations;
	}

	private List<Discussion> getFilteredDiscussions(Date dateFrom, Date dateTo,
			Integer values, List<Discussion> discussions) {
		for (Discussion discussion : discussions) {		
			this.filterMessages(discussion.getMessages(), dateFrom, dateTo);
		}
		this.sortDiscussionByAmountOfMessages(discussions);
		
		List<Discussion> filteredDiscussions = (discussions.size() >= values) ? discussions.subList(0, values) : discussions;
		return filteredDiscussions;
	}

	private void sortDiscussionByAmountOfMessages(List<Discussion> discussions) {
		Collections.sort(discussions, new Comparator<Discussion>(){
			@Override
			public int compare(Discussion aDiscussion, Discussion anotherDiscussion) {
				Integer aDiscussionAmountOfMessages = Integer.valueOf(aDiscussion.getMessages().size());
				Integer anotherDiscussionAmountOfMessages = Integer.valueOf(anotherDiscussion.getMessages().size());
				return anotherDiscussionAmountOfMessages.compareTo(aDiscussionAmountOfMessages);
			}
		});
	}

	private void filterMessages(Set<DiscussionMessage> messages, final Date dateFrom, final Date dateTo) {
		CollectionUtils.filter(messages, new Predicate() {
			@Override
			public boolean evaluate(Object o) {
				DiscussionMessage message = (DiscussionMessage)o;
				return message.getCreationDate().after(dateFrom) && message.getCreationDate().before(dateTo);
			}
		});
	}
}