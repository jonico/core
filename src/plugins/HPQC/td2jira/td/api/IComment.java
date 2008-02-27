package td2jira.td.api;

import java.io.Serializable;

public interface IComment extends Serializable {
	String getAuthor();
	String getCreated();
	String getBody();
	void setAuthor(String author);
	void setCreated(String created);
	void setBody(String body);
}
