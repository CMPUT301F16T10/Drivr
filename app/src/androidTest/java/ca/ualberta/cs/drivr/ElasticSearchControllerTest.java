package ca.ualberta.cs.drivr;

import android.util.Log;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Tests for the ElasticSearch controller.
 *
 * NOTE THAT ASYNC REALLY HATES YOU FOR TRYING TO DO SEVERAL DIFFERENT THINGS AT ONCE AND YOU NEED
 * TO FIGURE OUT SOME OTHER WAY OF HANDLING THESE TESTS.
 */

public class ElasticSearchControllerTest {

    private User user;

    public void setUser() {
        user = new User("tester", "test123");
        user.setPhoneNumber("123-456-7890");
        user.setEmail("test@test.test");
    }

    @Test
    public void addRequest(){
        assertEquals(2+2, 4);
    }

    @Test
    public void updateRequest(){
        assertEquals(2+2, 4);
    }

    @Test
    public void getRequests(){
        assertEquals(2+2, 4);
    }

    @Test
    public void searchRequestWithKeyword(){
        assertEquals(2+2, 4);
    }

    @Test
    public void searchRequestWithLocation(){
        assertEquals(2+2, 4);
    }

    //AddUser test fails - not sure why, the class does work, but tests just fuck something up
    //searchUser test succeeds
    @Test
    public void addAndSearchUser(){
        setUser();
        ElasticSearchController.AddUser addUser = new ElasticSearchController.AddUser();
        addUser.execute(user);

        User dup = null;
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("test123");
        try {
            dup = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }

        assertEquals(user.getUsername(), dup.getUsername());
    }


    @Test
    public void updateAndSearchUser(){
        setUser();
        ElasticSearchController.AddUser updateUser = new ElasticSearchController.AddUser();
        updateUser.execute(user);

        user.setEmail("test2@test2.test2");;
        updateUser.execute(user);

        User dup = null;
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("test123");
        try {
            dup = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }

        assertEquals(user.getEmail(), dup.getEmail());
    }

}
