package liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Power {

	@Id
	private ObjectId id;
	
	private Long collaborateurIdFrom;
	
	private Long collaborateurIdTo;

	public Power() {
		id = new ObjectId();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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
