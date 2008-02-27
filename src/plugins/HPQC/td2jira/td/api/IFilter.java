package td2jira.td.api;


public interface IFilter extends ILifeCycle {
	void setFilter(String field, String value);
	IFactoryList getNewList();
	IFactoryList getNewFilteredList(String field, String value);
	void refresh();
	void clear();
	IFactoryList getFields();
	String getText();
}