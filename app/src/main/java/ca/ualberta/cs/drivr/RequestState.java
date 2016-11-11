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
 * An enum that tracks the states or requests.
 */
public enum RequestState {
    /** A item_request made the by a rider but not yet accepted by a driver. */
    PENDING,

    /** A item_request that has been confirmed by the driver. */
    ACCEPTED,

    /** The rider confirms the driver's acceptance. The item_request is now ready to be fulfilled. */
    CONFIRMED,

    /** The rider has rejected a driver who has accepted. */
    DECLINED,

    /** The rider cancelled the item_request, indicating that they no longer want the ride. */
    CANCELLED,

    /** The item_request has been fulfilled. */
    COMPLETED
}
