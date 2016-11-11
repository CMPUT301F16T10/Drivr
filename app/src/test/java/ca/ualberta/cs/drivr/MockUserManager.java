/*
 * Copyright 2016 CMPUT301F16T10
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package ca.ualberta.cs.drivr;

/**
 * Created by adam on 2016-11-10.
 */

public class MockUserManager implements IUserManager {

    private User user;
    private UserMode userMode;
    private RequestsList requestsList;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public RequestsList getRequestsList() { return requestsList; }
    public void setRequestsList(RequestsList requestsList) { this.requestsList = requestsList; }
    public UserMode getUserMode() { return userMode; }
    public void setUserMode(UserMode userMode) { this.userMode = userMode; }
}
