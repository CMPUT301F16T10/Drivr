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
 * A controller for modifying lists of requests.
 * @see RequestsList
 */
public class RequestsListController {

    /**
     * The user manager that does the operations on item_request lists.
     */
    private UserManager userManager;

    /**
     * Instantiate a new RequestListController.
     * @param userManager The manager to perform operations with.
     */
    public RequestsListController(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Adds a request to the requests list.
     * The request to add.
     * @param request
     */
    public void addRequest(Request request) {
        userManager.getRequestsList().add(request);
        userManager.notifyObservers();
    }
}
