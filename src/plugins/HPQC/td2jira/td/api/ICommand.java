package td2jira.td.api;

public interface ICommand extends ILifeCycle {
    public void setCommandText(String cmd);
    public IRecordSet execute();
}