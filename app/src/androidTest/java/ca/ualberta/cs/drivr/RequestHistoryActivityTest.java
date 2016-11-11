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

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.robotium.solo.Solo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestHistoryActivityTest extends ActivityInstrumentationTestCase2<RequestHistoryActivity> {

    private Solo solo;

    public RequestHistoryActivityTest() {
        super(ca.ualberta.cs.drivr.RequestHistoryActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testClickOnRequest() {
        solo.assertCurrentActivity("Expected RequestHistoryActivity", RequestHistoryActivity.class);
        final Request request = getActivity().getRequest(0);
        solo.clickInList(0);
        solo.assertCurrentActivity("Expected RequestActivity", RequestActivity.class);
        //assertTrue(solo.waitForText(request.getDestination().toString()));
        solo.goBack();
    }

    public void testDeleteRequest() {
        solo.assertCurrentActivity("Expected RequestHistoryActivity", RequestHistoryActivity.class);
        final RequestsList requestsList = getActivity().getRequestsList();
        final int initialRequestsListSize = requestsList.size();
        Request request = getActivity().getRequest(0);
        View listItem = ((ListView) solo.getView("Requests list")).getChildAt(0);
        solo.clickOnButton(0 /* ID of delete button */);
        assertEquals(initialRequestsListSize - 1, requestsList.size());
    }

}
