package td2jira.td.api.mock;

import td2jira.td.api.IFactoryList;
import td2jira.td.api.IFilter;

public class Filter implements IFilter {
	public void clear() {
	}

	public IFactoryList getFields() {
		return new FactoryList();
	}

	public IFactoryList getNewFilteredList(String field, String value) {
		return new FactoryList();
	}

	public IFactoryList getNewList() {
		return new FactoryList();
	}

	public String getText() {
		return "";
	}

	public void refresh() {
	}

	public void setFilter(String field, String value) {
	}

	public void safeRelease() {
	}
}
