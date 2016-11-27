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

import android.content.Context;
import android.net.ConnectivityManager;

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
     * Determines whether a request can be accepted by the user. Requests can be accepted by a
     * driver when the request state is pending.
     * @param request The request
     * @param userMode The user mode of the current user
     * @return True when the request can be accepted, false otherwise.
     */
    public boolean canAcceptRequest(Request request, UserMode userMode) {
        if (request.getRequestState() == RequestState.PENDING && userMode == UserMode.DRIVER)
            return true;
        return false;
    }

    /**
     * Accept a request.
     * @param request The request to accept.
     */
    public void acceptRequest(Request request, Context context) {
        ElasticSearch elasticSearch = new ElasticSearch((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        elasticSearch.updateRequest(request);
        request.setRequestState(RequestState.ACCEPTED);
        UserManager.getInstance().getRequestsList().add(request);
        UserManager.getInstance().notifyObservers();
    }

    /**
     * Determines whether a request can be cancelled by the user. Requests can be cancelled by a
     * rider when the request state is pending.
     * @param request The request
     * @param userMode The user mode of the current user
     * @return True when the request can be cancelled, false otherwise.
     */
    public boolean canCancelRequest(Request request, UserMode userMode) {
        if (request.getRequestState() == RequestState.PENDING && userMode == UserMode.RIDER)
            return true;
        return false;
    }

    /**
     * Cancel a request.
     * @param request The request to cancel.
     */
    public void cancelRequest(Request request) {
        request.setRequestState(RequestState.CANCELLED);
    }

    /**
     * Determines whether a request can be completed by the user. Requests can be completed by a
     * rider when the request state is confirmed.
     * @param request The request
     * @param userMode The user mode of the current user
     * @return True when the request can be confirmed, false otherwise.
     */
    public boolean canCompleteRequest(Request request, UserMode userMode) {
        if (request.getRequestState() == RequestState.CONFIRMED && userMode == UserMode.RIDER)
            return true;
        return false;
    }

    /**
     * Complete a request.
     * @param request The request to complete.
     */
    public void completeRequest(Request request) {
        request.setRequestState(RequestState.COMPLETED);
        userManager.notify();
    }

    /**
     * Determines whether a request can be confirmed by the user. Requests can be confirmed by a
     * rider when the request state is accepted by a driver.
     * @param request The request
     * @param userMode The user mode of the current user
     * @return True when the request can be confirmed, false otherwise.
     */
    public boolean canConfirmRequest(Request request, UserMode userMode) {
        if (request.getRequestState() == RequestState.ACCEPTED && userMode == UserMode.RIDER)
            return true;
        return false;
    }

    /**
     * Confirm a request.
     * @param request The request to confirm.
     */
    public void confirmRequest(Request request) {
        request.setRequestState(RequestState.CONFIRMED);
    }

    /**
     * Determines whether a request can be declined by the user. Requests can be declined by a
     * rider when the request state is accepted by a driver.
     * @param request The request
     * @param userMode The user mode of the current user
     * @return True when the request can be declined, false otherwise.
     */
    public boolean canDeclineRequest(Request request, UserMode userMode) {
        if (request.getRequestState() == RequestState.ACCEPTED && userMode == UserMode.RIDER)
            return true;
        return false;
    }

    /**
     * Decline a request.
     * @param request The request to decline.
     */
    public void declineRequest(Request request) {
        request.setRequestState(RequestState.DECLINED);
    }

    /**
     * Determines whether a request can be deleted by the user. Requests can be deleted by a
     * rider when the request is pending, cancelled, declined, or completed. A request can be
     * deleted by a driver when the request is cancelled, declined, or completed.
     * @param request The request
     * @param userMode The user mode of the current user
     * @return True when the request can be declined, false otherwise.
     */
    public boolean canDeleteRequest(Request request, UserMode userMode) {
        // For riders and drivers => we don't care about user mode
        if (request.getRequestState() == RequestState.CANCELLED
                || request.getRequestState() == RequestState.DECLINED
                || request.getRequestState() == RequestState.COMPLETED)
            return true;
        // Just for riders
        if (request.getRequestState() == RequestState.PENDING && userMode == UserMode.RIDER)
            return true;
        return false;
    }

    /**
     * Delete a request from from the manager.
     * @param request The request to delete.
     */
    public void deleteRequest(Request request) {
        userManager.getRequestsList().remove(request);
    }
}
