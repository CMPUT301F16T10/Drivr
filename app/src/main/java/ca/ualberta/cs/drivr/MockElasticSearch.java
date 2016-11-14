package ca.ualberta.cs.drivr;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;

/**
 * Created by colton on 2016-11-14.
 */

public class MockElasticSearch {
    private ArrayList<User> userArrayList;
    private static final MockElasticSearch instance = new MockElasticSearch();

    public static MockElasticSearch getInstance() {
        return instance;
    }
    public MockElasticSearch() {
        userArrayList = new ArrayList<>();
    }


    public boolean saveUser(User user) {
        for (User savedUsers : userArrayList) {
            if (savedUsers.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        userArrayList.add(user);
        return true;
    }
    public User loadUser(String username) {
        for (User savedUser : userArrayList) {
            if (savedUser.getUsername().equals(username)) {
                return savedUser;
            }
        }
        return null;
    }
    public void updateUser(User user) {
        for (User savedUser: userArrayList) {
            if (savedUser.getUsername().equals(user.getUsername())) {
                userArrayList.remove(savedUser);
                userArrayList.add(user);
                return;
            }
        }
    }



}
