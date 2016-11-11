/*
 *  Copyright 2016 CMPUT301F16T10
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ca.ualberta.cs.drivr;

/**
 * A controller for modifying requests.
 * @see Request
 */
public class RequestController {

    /**
     * The user manager that does the operations on requests.
     */
    private IUserManager userManager;

    /**
     * Instantiate a new RequestController.
     * @param userManager The manager to perform operations with.
     */
    public RequestController(IUserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Accept a item_request.
     * @param request The item_request to accept.
     */
    public void acceptRequest(Request request) {
        request.setRequestState(RequestState.ACCEPTED);
    }

    /**
     * Cancel a item_request.
     * @param request The item_request to cancel.
     */
    public void cancelRequest(Request request) {
        request.setRequestState(RequestState.CANCELLED);
    }

    /**
     * Complete a item_request.
     * @param request The item_request to complete.
     */
    public void completeRequest(Request request) {
        request.setRequestState(RequestState.COMPLETED);
    }

    /**
     * Confirm a item_request.
     * @param request The item_request to confirm.
     */
    public void confirmRequest(Request request) {
        request.setRequestState(RequestState.CONFIRMED);
    }

    /**
     * Decline a item_request.
     * @param request The item_request to decline.
     */
    public void declineRequest(Request request) {
        request.setRequestState(RequestState.DECLINED);
    }

    /**
     * Delete a item_request from from the manager.
     * @param request The item_request to delete.
     */
    public void deleteRequest(Request request) {
        userManager.getRequestsList().remove(request);
    }
}
