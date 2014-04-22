/*
 * Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.idp.mgt.ui.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.model.idp.xsd.FederatedIdentityProvider;
import org.wso2.carbon.identity.application.common.model.idp.xsd.ResidentIdentityProvider;
import org.wso2.carbon.idp.mgt.stub.IdentityProviderMgtServiceStub;
import org.wso2.carbon.user.mgt.stub.UserAdminStub;

public class IdentityProviderMgtServiceClient {

    private static Log log = LogFactory.getLog(IdentityProviderMgtServiceClient.class);

    private IdentityProviderMgtServiceStub idPMgtStub;

    private UserAdminStub userAdminStub;

    /**
     * @param cookie HttpSession cookie
     * @param backendServerURL Backend Carbon server URL
     * @param configCtx Axis2 Configuration Context
     */
    public IdentityProviderMgtServiceClient(String cookie, String backendServerURL,
            ConfigurationContext configCtx) {

        String idPMgtServiceURL = backendServerURL + "IdentityProviderMgtService";
        String userAdminServiceURL = backendServerURL + "UserAdmin";
        try {
            idPMgtStub = new IdentityProviderMgtServiceStub(configCtx, idPMgtServiceURL);
        } catch (AxisFault axisFault) {
            log.error("Error while instantiating IdentityProviderMgtServiceStub", axisFault);
        }
        try {
            userAdminStub = new UserAdminStub(configCtx, userAdminServiceURL);
        } catch (AxisFault axisFault) {
            log.error("Error while instantiating UserAdminServiceStub", axisFault);
        }
        ServiceClient idPMgtClient = idPMgtStub._getServiceClient();
        ServiceClient userAdminClient = userAdminStub._getServiceClient();
        Options idPMgtOptions = idPMgtClient.getOptions();
        idPMgtOptions.setManageSession(true);
        idPMgtOptions.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                cookie);
        Options userAdminOptions = userAdminClient.getOptions();
        userAdminOptions.setManageSession(true);
        userAdminOptions.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                cookie);
    }

    /**
     * Retrieves Resident Identity provider for a given tenant
     * 
     * @return <code>ResidentIdentityProvider</code>
     * @throws Exception Error when getting Resident Identity Providers
     */
    public ResidentIdentityProvider getResidentIdP() throws Exception {
        try {
            return idPMgtStub.getResidentIdP();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while retrieving list of Identity Providers");
        }
    }

    /**
     * Updated Resident Identity provider for a given tenant
     * 
     * @return <code>ResidentIdentityProvider</code>
     * @throws Exception Error when getting Resident Identity Providers
     */
    public void updateResidentIdP(ResidentIdentityProvider residentIdentityProvider)
            throws Exception {
        try {
            ResidentIdentityProvider resIdP = new ResidentIdentityProvider();
            resIdP.setHomeRealmId(residentIdentityProvider.getHomeRealmId());
            resIdP.setOpenIdRealm(residentIdentityProvider.getOpenIdRealm());
            resIdP.setIdpEntityId(residentIdentityProvider.getIdpEntityId());
            resIdP.setPassiveSTSRealm(residentIdentityProvider.getPassiveSTSRealm());
            idPMgtStub.updateResidentIdP(resIdP);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while retrieving list of Identity Providers");
        }
    }

    /**
     * Retrieves registered Identity providers for a given tenant
     * 
     * @return List of <code>FederatedIdentityProvider</code>. IdP names, primary IdP and home realm
     *         identifiers of each IdP
     * @throws Exception Error when getting list of Identity Providers
     */
    public List<FederatedIdentityProvider> getIdPs() throws Exception {
        try {
            FederatedIdentityProvider[] identityProviders = idPMgtStub.getAllIdPs();
            if (identityProviders != null && identityProviders.length > 0) {
                return Arrays.asList(identityProviders);
            } else {
                return new ArrayList<FederatedIdentityProvider>();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while retrieving list of Identity Providers");
        }
    }

    /**
     * Retrieves Identity provider information about a given tenant by Identity Provider name
     * 
     * @param idPName Unique name of the Identity provider of whose information is requested
     * @return <code>FederatedIdentityProvider</code> Identity Provider information
     * @throws Exception Error when getting Identity Provider information by IdP name
     */
    public FederatedIdentityProvider getIdPByName(String idPName) throws Exception {
        try {
            return idPMgtStub.getIdPByName(idPName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while retrieving information about " + idPName);
        }
    }

    /**
     * Adds an Identity Provider to the given tenant
     * 
     * @param identityProvider <code><FederatedIdentityProvider/code></code> federated Identity
     *        Provider information
     * @throws Exception Error when adding Identity Provider information
     */
    public void addIdP(FederatedIdentityProvider identityProvider) throws Exception {

        try {
            idPMgtStub.addIdP(identityProvider);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while adding Identity Provider "
                    + identityProvider.getIdentityProviderName());
        }
    }

    /**
     * Deletes an Identity Provider from a given tenant
     * 
     * @param idPName Name of the IdP to be deleted
     * @throws Exception Error when deleting Identity Provider information
     */
    public void deleteIdP(String idPName) throws Exception {
        try {
            idPMgtStub.deleteIdP(idPName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while deleting Identity Provider " + idPName);
        }
    }

    /**
     * Updates a given Identity Provider information
     * 
     * @param oldIdPName existing IdP name
     * @param identityProvider <code>FederatedIdentityProvider</code> new IdP information
     * @throws Exception Error when updating Identity Provider information
     */
    public void updateIdP(String oldIdPName, FederatedIdentityProvider identityProvider)
            throws Exception {
        try {
            idPMgtStub.updateIdP(oldIdPName, identityProvider);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while deleting Identity Provider " + oldIdPName);
        }
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String[] getAllLocalClaimUris() throws Exception {

        try {
            return idPMgtStub.getAllLocalClaimUris();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Error occurred while retrieving all local claim URIs");
        }
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String[] getUserStoreDomains() throws Exception {

        try {
            return userAdminStub.getUserRealmInfo().getDomainNames();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(
                    "Error occurred while retrieving User Store Domain IDs for logged-in user's tenant realm");
        }
    }

}