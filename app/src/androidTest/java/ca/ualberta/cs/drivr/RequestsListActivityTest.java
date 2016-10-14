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

import android.test.InstrumentationTestCase;

import com.robotium.solo.Solo;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestsListActivityTest extends ActivityInstrumentationTestCase2<RequestsListActivity> {

    private Solo solo;

    public RequestsListActivityTest() {
        super(ca.ualberta.cs.drivr.RequestsListActivity.class);
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
        solo.assertCurrentActivity("Expected RequestsListActivity", RequestsListActivity.class);
        final Request request = getActivity().getRequest(0);
        solo.clickInList(0);
        solo.assertCurrentActivity("Expected RequestActivity", RequestActivity.class);
        assertTrue(solo.waitForText(request.getDestination().toString()));
        solo.goBack();
    }

    public void testDeleteRequest() {
        solo.assertCurrentActivity("Expected RequestsListActivity", RequestsListActivity.class);
        final RequestsList requestsList = getActivity().getRequestsList();
        final int initialRequestsListSize = requestsList.size();
        Request request = getActivity().getRequest(0);
        View listItem = ((ListView) solo.getView("Requests list")).getChildAt(0);
        solo.clickOnButton(0 /* ID of delete button */);
        assertEquals(initialRequestsListSize - 1, requestsList.size());
    }

    /**
     * UC 5 Rider Contacts Driver
     * US 01.05.01 As a Rider, I want to be able to phone or email the Driver who Accepted a Request.
     */


    public void testCallDriver() {
        solo.assertCurrentActivity("Expected RequestsListActivity", RequestsListActivity.class);
        ListView listView = (ListView)solo.getView("pending requests");
        View view = listView.getChildAt(0);
        Button callButton = (Button) view.findViewById(0 /* Call driver button */);
        solo.clickOnView(callButton);
        assertFalse(solo.getCurrentActivity() instanceof RequestsListActivity);
        solo.goBack();
    }

    public void testEmaiDriver() {
        solo.assertCurrentActivity("Expected RequestsListActivity", RequestsListActivity.class);
        ListView listView = (ListView)solo.getView("pending requests");
        View view = listView.getChildAt(0);
        Button emailButton = (Button) view.findViewById(0 /* Email driver button */);
        solo.clickOnView(emailButton);
        assertFalse(solo.getCurrentActivity() instanceof RequestsListActivity);
        solo.goBack();
    }

}

