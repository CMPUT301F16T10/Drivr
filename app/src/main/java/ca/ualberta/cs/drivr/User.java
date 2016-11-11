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

public class User {

    private PrivateUserInfo privateInfo;
    private PublicUserInfo publicInfo;
    private String userId;

    public User() { throw new UnsupportedOperationException(); }

    public User(String userName, String email, String phoneNumber) { throw new UnsupportedOperationException(); }

    public PrivateUserInfo getPrivateInfo() {
        return privateInfo;
    }

    public void setPrivateInfo(PrivateUserInfo privateInfo) {
        this.privateInfo = privateInfo;
    }

    public PublicUserInfo getPublicInfo() {
        return publicInfo;
    }

    public void setPublicInfo(PublicUserInfo publicInfo) {
        this.publicInfo = publicInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          