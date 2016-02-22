package liquid.democracy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Power {

	@Id
	private Long id;
	
	@DBRef
	private Subject subject;
	
	private Long collaborateurIdFrom;
	
	private Long collaborateurIdTo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Long getCollaborateurIdFrom() {
		return collaborateurIdFrom;
	}

	public void setCollaborateurIdFrom(Long collaborateurIdFrom) {
		this.collaborateurIdFrom = collaborateurIdFrom;
	}

	public Long getCollaborateurIdTo() {
		return collaborateurIdTo;
	}

	public void setCollaborateurIdTo(Long collaborateurIdTo) {
		this.collaborateurIdTo = collaborateurIdTo;
	}
	
}
