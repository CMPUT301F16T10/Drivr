package ca.ualberta.cs.drivr;

/**
 * The object used in ElasticSearch.
 */

public class Driver {
    private String username;
    private RequestState status;

    /**
     * Constructor for Driver.
     */
    public Driver() {
        username = "";
        status = RequestState.PENDING;
    }

    /**
     * Returns username of driver.
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username of driver.
     * @param username Driver's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns status of driver.
     * @return RequestState
     */
    public RequestState getStatus() {
        return status;
    }

    /**
     * Sets status of driver.
     * @param status Driver's status.
     */
    public void setStatus(RequestState status) {
        this.status = status;
    }
}
