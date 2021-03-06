/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.identity.relyingparty.dto;

import java.util.Arrays;

public class OpenIDDTO {

    private String openID;
    private String userName;
    private String password;
    private ClaimDTO[] claims;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID.trim();
    }

    public ClaimDTO[] getClaims() {
        return Arrays.copyOf(claims, claims.length);
    }

    public void setClaims(ClaimDTO[] claims) {
        this.claims = Arrays.copyOf(claims, claims.length);
    }

}
