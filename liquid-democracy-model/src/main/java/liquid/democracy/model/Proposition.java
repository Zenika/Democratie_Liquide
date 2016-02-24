package liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

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

	public boolean isWellFormed() {
		boolean result = !StringUtils.isEmpty(title);

		return result;
	}

	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder();

		strB.append("Proposition : ").append(getTitle());
		strB.append("\n \t Id : ").append(getId());
		strB.append("\n \t Title : ").append(getTitle());
		strB.append("\n \t Description : ").append(getDescription());

		return strB.toString();
	}

}
