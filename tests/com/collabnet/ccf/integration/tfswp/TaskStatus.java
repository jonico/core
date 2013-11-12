package com.collabnet.ccf.integration.tfswp;

/**
 * Task statuses in ScrumWorks.
 * 
 * @author Kelley
 * 
 */

public enum TaskStatus {

    /** The task has not been started. */
    NOT_STARTED("Not Started"),

    /** The task is in progress. */
    IN_PROGRESS("In Progress"),

    /** The task is impeded. */
    IMPEDED("Impeded"),

    /** The task is done. */
    DONE("Done");

    /** The status. */
    private final String status;

    /**
     * Instantiates a new {@link TaskStatus}.
     * 
     * @param status
     *            the status
     */
    private TaskStatus(final String status) {
        assert status != null : "null status";

        this.status = status;
    }

    /**
     * Return the status.
     * 
     * @return the status
     */
    public String getStatus() {
        return status;
    }
}
