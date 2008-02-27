package td2jira.td.api;

import java.io.Serializable;

public interface ILifeCycle extends Serializable {
	void safeRelease();
}
