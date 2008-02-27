package td2jira.td.api;

public interface IRecordSet extends ILifeCycle {
	public int getRecordCount();
    public int getColCount();
    public String getFieldAsString(String field);
    public String getFieldValue(String fieldName);
    public String getColNameAsString(int index);
    public String getColName(int index);
    public void next();
}