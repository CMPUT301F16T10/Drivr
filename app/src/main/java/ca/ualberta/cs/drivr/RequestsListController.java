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
 * Created by adam on 2016-10-12.
 */

public class RequestsListController {

    private IUserManager userManager;

    public RequestsListController(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void acceptRequest(Request request) {
        request.setRequestState(RequestState.ACCEPTED);
    }

    public void confirmRequest(Request request) {
        request.setRequestState(RequestState.CONFIRMED);
    }

    public void declineRequest(Request request) {
        request.setRequestState(RequestState.DECLINED);
    }

    public void cancelRequest(Request request) {
        request.setRequestState(RequestState.CANCELLED);
    }

    public void completeRequest(Request request) {
        request.setRequestState(RequestState.COMPLETED);
    }
}
