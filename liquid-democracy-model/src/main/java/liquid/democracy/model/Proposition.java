package liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Proposition {
	
	@Id
	private ObjectId id;
	
	private String title;
	
	private String description;
	
	public Proposition() {
		id = new ObjectId();
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
