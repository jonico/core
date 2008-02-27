package td2jira.td.api.mock;

import td2jira.td.api.IFactory;
import td2jira.td.api.IFilter;

public class Factory implements IFactory {
	public IFilter getFilter() {
		return new Filter();
	}

	public void safeRelease() {
	}
}
