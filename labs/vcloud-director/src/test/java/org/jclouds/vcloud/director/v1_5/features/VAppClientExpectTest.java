/*
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 *(Link.builder().regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless(Link.builder().required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.features;

import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.jclouds.dmtf.ovf.NetworkSection;
import org.jclouds.dmtf.ovf.StartupSection;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.Error;
import org.jclouds.vcloud.director.v1_5.domain.Link;
import org.jclouds.vcloud.director.v1_5.domain.Owner;
import org.jclouds.vcloud.director.v1_5.domain.ProductSectionList;
import org.jclouds.vcloud.director.v1_5.domain.RasdItemsList;
import org.jclouds.vcloud.director.v1_5.domain.Reference;
import org.jclouds.vcloud.director.v1_5.domain.ScreenTicket;
import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.domain.VApp;
import org.jclouds.vcloud.director.v1_5.domain.VmPendingQuestion;
import org.jclouds.vcloud.director.v1_5.domain.VmQuestionAnswer;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.RasdItem;
import org.jclouds.vcloud.director.v1_5.domain.params.ControlAccessParams;
import org.jclouds.vcloud.director.v1_5.domain.params.DeployVAppParams;
import org.jclouds.vcloud.director.v1_5.domain.params.RecomposeVAppParams;
import org.jclouds.vcloud.director.v1_5.domain.params.UndeployVAppParams;
import org.jclouds.vcloud.director.v1_5.domain.section.GuestCustomizationSection;
import org.jclouds.vcloud.director.v1_5.domain.section.LeaseSettingsSection;
import org.jclouds.vcloud.director.v1_5.domain.section.NetworkConfigSection;
import org.jclouds.vcloud.director.v1_5.domain.section.NetworkConnectionSection;
import org.jclouds.vcloud.director.v1_5.domain.section.OperatingSystemSection;
import org.jclouds.vcloud.director.v1_5.domain.section.RuntimeInfoSection;
import org.jclouds.vcloud.director.v1_5.domain.section.VirtualHardwareSection;
import org.jclouds.vcloud.director.v1_5.internal.VCloudDirectorAdminClientExpectTest;
import org.jclouds.vcloud.director.v1_5.user.VCloudDirectorClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.annotations.Sets;

/**
 * Allows us to test the {@link VAppClient} via its side effects.
 * 
 * @author grkvlt@apache.org
 */
@Test(groups = { "unit", "user" }, singleThreaded = true, testName = "VAppClientExpectTest")
public class VAppClientExpectTest extends VCloudDirectorAdminClientExpectTest {
   
   private String vAppId = "vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be";
   private URI vAppURI = URI.create(endpoint + vAppId);
   
   @BeforeClass
   public void before() {
   }
   
   @Test(enabled = false)//TODO
   public void testGetVapp() {
      VCloudDirectorClient client = orderedRequestsSendResponses(loginRequest, sessionResponse, 
            new VcloudHttpRequestPrimer()
               .apiCommand("GET", vAppId)
               .acceptMedia(VCloudDirectorMediaType.VAPP)
               .httpRequestBuilder().build(), 
            new VcloudHttpResponsePrimer()
               .xmlFilePayload("/vApp/vApp.xml", VCloudDirectorMediaType.VAPP)
               .httpResponseBuilder().build());
      
      VApp expected = getVApp();

      assertEquals(client.getVAppClient().getVApp(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testModifyVApp() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("PUT", vAppId)
            .xmlFilePayload("/vApp/modifyVApp.xml", VCloudDirectorMediaType.VAPP)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/modifiedVapp.xml", VCloudDirectorMediaType.VAPP)
            .httpResponseBuilder().build());
		         
		VApp modified = getVApp();
		modified.setName("new-name");
		modified.setDescription("New Description");
		
		Task expected = modifyVAppTask();
		
		assertEquals(client.getVAppClient().modifyVApp(vAppURI, modified), expected);
   }

   @Test(enabled = false)
   public void testDeleteVApp() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("DELETE", vAppId)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/deleteVAppTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
		
		Task expected = deleteVAppTask();
		
		assertEquals(client.getVAppClient().deleteVApp(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testControlAccess() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/controlAccess")
            .xmlFilePayload("/vApp/controlAccessParams.xml", VCloudDirectorMediaType.CONTROL_ACCESS)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/controlAccessParams.xml", VCloudDirectorMediaType.CONTROL_ACCESS)
            .httpResponseBuilder().build());
		
		ControlAccessParams params = controlAccessParams();
		
		ControlAccessParams expected = controlAccessParams();
		         
		assertEquals(client.getVAppClient().modifyControlAccess(vAppURI, params), expected);
   }

   @Test(enabled = false)
   public void testDeploy() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/deploy")
            .xmlFilePayload("/vApp/deployParams.xml", VCloudDirectorMediaType.DEPLOY_VAPP_PARAMS)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/deployTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
      
      DeployVAppParams params = DeployVAppParams.builder()
            .build();
		
		Task expected = deployTask();
		
		assertEquals(client.getVAppClient().deploy(vAppURI, params), expected);
   }

   @Test(enabled = false)
   public void testDiscardSuspendedState() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/discardSuspendedState")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/discardSuspendedStateTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
		
		Task expected = discardSuspendedStateTask();
		
		assertEquals(client.getVAppClient().discardSuspendedState(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testEnterMaintenanceMode() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/enterMaintenanceMode")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .httpResponseBuilder().statusCode(204).build());
		
		// TODO how to test?
		client.getVAppClient().enterMaintenanceMode(vAppURI);
   }

   @Test(enabled = false)
   public void testExitMaintenanceMode() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/exitMaintenanceMode")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .httpResponseBuilder().statusCode(204).build());
		
		// TODO how to test?
		client.getVAppClient().exitMaintenanceMode(vAppURI);
   }

   @Test(enabled = false)
   public void testRecomposeVApp() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/recomposeVApp")
            .xmlFilePayload("/vApp/recomposeVAppParams.xml", VCloudDirectorMediaType.RECOMPOSE_VAPP_PARAMS)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/recomposeVAppTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
     
      RecomposeVAppParams params = RecomposeVAppParams.builder()
            .build();
		
		Task expected = recomposeVAppTask();
		
		assertEquals(client.getVAppClient().recompose(vAppURI, params), expected);
   }

   @Test(enabled = false)
   public void testUndeploy() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/action/undeploy")
            .xmlFilePayload("/vApp/undeployParams.xml", VCloudDirectorMediaType.UNDEPLOY_VAPP_PARAMS)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/undeployTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
      
      UndeployVAppParams params = UndeployVAppParams.builder()
            .build();
		
		Task expected = undeployTask();
		
		assertEquals(client.getVAppClient().undeploy(vAppURI, params), expected);
   }

   @Test(enabled = false)
   public void testPowerOff() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/power/action/powerOff")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/powerOffTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());

      Task expected = powerOffTask();

      assertEquals(client.getVAppClient().powerOff(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testPowerOn() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/power/action/powerOn")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/powerOnTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());

      Task expected = powerOnTask();

      assertEquals(client.getVAppClient().powerOn(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testReboot() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/power/action/reboot")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/rebootTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());

      Task expected = rebootTask();

      assertEquals(client.getVAppClient().reboot(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testReset() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/power/action/reset")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/resetTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());

      Task expected = resetTask();

      assertEquals(client.getVAppClient().reset(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testShutdown() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/power/action/shutdown")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/shutdownTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());

      Task expected = shutdownTask();

      assertEquals(client.getVAppClient().shutdown(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testSuspend() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("POST", vAppId + "/power/action/suspend")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/suspend.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
		
		Task expected = suspendTask();
		
		assertEquals(client.getVAppClient().suspend(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testGetControlAccesss() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/controlAccess")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getControlAccess.xml", VCloudDirectorMediaType.VAPP)
            .httpResponseBuilder().build());

      ControlAccessParams expected = getControlAccessParams();

      assertEquals(client.getVAppClient().getControlAccess(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testGetLeaseSettingsSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/leaseSettingsSection")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getLeaseSettingsSection.xml", VCloudDirectorMediaType.LEASE_SETTINGS_SECTION)
            .httpResponseBuilder().build());

      LeaseSettingsSection expected = getLeaseSettingsSection();

      assertEquals(client.getVAppClient().getLeaseSettingsSection(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testModifyLeaseSettingsSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("PUT", vAppId + "/leaseSettingsSection")
            .xmlFilePayload("/vApp/modifyLeaseSettingsSection.xml", VCloudDirectorMediaType.LEASE_SETTINGS_SECTION)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/modifyLeaseSettingsSectionTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
      
      LeaseSettingsSection section = getLeaseSettingsSection().toBuilder()
            .build();
		
		Task expected = modifyLeaseSettingsSectionTask();
		
		assertEquals(client.getVAppClient().modifyLeaseSettingsSection(vAppURI, section), expected);
   }

   @Test(enabled = false)
   public void testGetNetworkConfigSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/networkConfigSection")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getNetworkConfigSection.xml", VCloudDirectorMediaType.NETWORK_CONFIG_SECTION)
            .httpResponseBuilder().build());
			
		NetworkConfigSection expected = getNetworkConfigSection();
		
		assertEquals(client.getVAppClient().getNetworkConfigSection(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testModifyNetworkConfigSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("PUT", vAppId + "/networkConfigSection")
            .xmlFilePayload("/vApp/modifyNetworkConfigSection.xml", VCloudDirectorMediaType.NETWORK_CONFIG_SECTION)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/modifyNetworkConfigSectionTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
		      
		NetworkConfigSection section = getNetworkConfigSection().toBuilder()
		      .build();
		
		Task expected = modifyNetworkConfigSectionTask();
		
		assertEquals(client.getVAppClient().modifyNetworkConfigSection(vAppURI, section), expected);
   }

   @Test(enabled = false)
   public void testGetNetworkSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/networkSection")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getNetworkSection.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
		
		NetworkSection expected = getNetworkSection();
		
		assertEquals(client.getVAppClient().getNetworkSection(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testGetOwner() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/owner")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getOwner.xml", VCloudDirectorMediaType.OWNER)
            .httpResponseBuilder().build());

         Owner expected = getOwner();

         assertEquals(client.getVAppClient().getOwner(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testModifyOwner() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("PUT", vAppId + "/owner")
            .xmlFilePayload("/vApp/modifyOwner.xml", VCloudDirectorMediaType.OWNER)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .httpResponseBuilder().statusCode(204).build());
      
      Owner owner = Owner.builder()
            .build();
		
		client.getVAppClient().modifyOwner(vAppURI, owner);
   }

   @Test(enabled = false)
   public void testGetProductSections() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/productSections")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getProductSections.xml", VCloudDirectorMediaType.PRODUCT_SECTION_LIST)
            .httpResponseBuilder().build());

         ProductSectionList expected = getProductSections();

         assertEquals(client.getVAppClient().getProductSections(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testModifyProductSections() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("PUT", vAppId + "/productSections")
            .xmlFilePayload("/vApp/modifyProductSections.xml", VCloudDirectorMediaType.PRODUCT_SECTION_LIST)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/modifyProductSections.xml", VCloudDirectorMediaType.VAPP)
            .httpResponseBuilder().build());

         Task expected = modifyProductSectionsTask();

         assertEquals(client.getVAppClient().modifyProductSections(vAppURI, null), expected);
   }

   @Test(enabled = false)
   public void testGetStartupSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("GET", vAppId + "/startupSection")
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/getStartupSection.xml", VCloudDirectorMediaType.STARTUP_SECTION)
            .httpResponseBuilder().build());
		
		StartupSection expected = getStartupSection();
		
		assertEquals(client.getVAppClient().getStartupSection(vAppURI), expected);
   }

   @Test(enabled = false)
   public void testModifyStartupSection() {
      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, 
         new VcloudHttpRequestPrimer()
            .apiCommand("PUT", vAppId + "/startupSection")
            .xmlFilePayload("/vApp/modifyStartupSection.xml", VCloudDirectorMediaType.STARTUP_SECTION)
            .acceptAnyMedia()
            .httpRequestBuilder().build(), 
         new VcloudHttpResponsePrimer()
            .xmlFilePayload("/vApp/modifyStartupSectionTask.xml", VCloudDirectorMediaType.TASK)
            .httpResponseBuilder().build());
      
      StartupSection section = null; // getStartupSection().toBuilder()
//            .build();
		
		Task expected = modifyStartupSectionTask();
		
		assertEquals(client.getVAppClient().modifyStartupSection(vAppURI, section), expected);
   }

   public static VApp getVApp() {
      // FIXME Does not match XML
      VApp vApp = VApp.builder()
            .href(URI.create("https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be"))
//            .link(Link.builder()
//                     .href(URI.create())
//                     .build())
            .build();

//      <Link rel="power:powerOn" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/power/action/powerOn"/>
//      <Link rel="deploy" type="application/vnd.vmware.vcloud.deployVAppParams+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/action/deploy"/>
//      <Link rel="down" type="application/vnd.vmware.vcloud.vAppNetwork+xml" name="orgNet-cloudsoft-External" href="https://mycloud.greenhousedata.com/api/network/2a2e2da4-446a-4ebc-a086-06df7c9570f0"/>
//      <Link rel="down" type="application/vnd.vmware.vcloud.controlAccess+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/controlAccess/"/>
//      <Link rel="controlAccess" type="application/vnd.vmware.vcloud.controlAccess+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/action/controlAccess"/>
//      <Link rel="recompose" type="application/vnd.vmware.vcloud.recomposeVAppParams+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/action/recomposeVApp"/>
//      <Link rel="up" type="application/vnd.vmware.vcloud.vdc+xml" href="https://mycloud.greenhousedata.com/api/vdc/e9cd3387-ac57-4d27-a481-9bee75e0690f"/>
//      <Link rel="edit" type="application/vnd.vmware.vcloud.vApp+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be"/>
//      <Link rel="remove" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be"/>
//      <Link rel="down" type="application/vnd.vmware.vcloud.owner+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/owner"/>
//      <Link rel="down" type="application/vnd.vmware.vcloud.metadata+xml" href="https://mycloud.greenhousedata.com/api/vApp/vapp-d0e2b6b9-4381-4ddc-9572-cdfae54059be/metadata"/>
      
      return vApp;
   }

   public static Task modifyVAppTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task deleteVAppTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task consolidateVAppTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static ControlAccessParams controlAccessParams() {
      ControlAccessParams params = ControlAccessParams.builder()
            .build();

      return params;
   }

   public static Task deployTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task discardSuspendedStateTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task installVMwareToolsTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task recomposeVAppTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task relocateTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task undeployTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task upgradeHardwareVersionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task powerOffTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task powerOnTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task rebootTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task resetTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task shutdownTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task suspendTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static ControlAccessParams getControlAccessParams() {
      ControlAccessParams params = ControlAccessParams.builder()
            .build();

      return params;
   }

   public static GuestCustomizationSection getGuestCustomizationSection() {
      GuestCustomizationSection section = GuestCustomizationSection.builder()
            .build();

      return section;
   }

   public static Task modifyGuestCustomizationSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static LeaseSettingsSection getLeaseSettingsSection() {
      LeaseSettingsSection section = LeaseSettingsSection.builder()
            .build();

      return section;
   }

   public static Task modifyLeaseSettingsSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task ejectMediaTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Task insertMediaTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static NetworkConfigSection getNetworkConfigSection() {
      NetworkConfigSection section = NetworkConfigSection.builder()
            .build();

      return section;
   }

   public static Task modifyNetworkConfigSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static NetworkConnectionSection getNetworkConnectionSection() {
      NetworkConnectionSection section = NetworkConnectionSection.builder()
            .build();

      return section;
   }

   public static Task modifyNetworkConnectionSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static NetworkSection getNetworkSection() {
      NetworkSection section = NetworkSection.builder()
            .build();

      return section;
   }

   public static OperatingSystemSection getOperatingSystemSection() {
      OperatingSystemSection section = OperatingSystemSection.builder()
            .build();

      return section;
   }

   public static Task modifyOperatingSystemSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static Owner getOwner() {
      Owner owner = Owner.builder()
            .build();

      return owner;
   }

   public static Task modifyOwnerTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static ProductSectionList getProductSections() {
      ProductSectionList sectionItems = ProductSectionList.builder()
            .build();

      return sectionItems;
   }

   public static Task modifyProductSectionsTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static VmPendingQuestion getPendingQuestion() {
      VmPendingQuestion question = VmPendingQuestion.builder()
            .build();

      return question;
   }

   public static VmQuestionAnswer answerQuestion() {
      VmQuestionAnswer answer = null; // = VmQuestionAnswer.builder() 
//            .build();

      return answer;
   }

   public static RuntimeInfoSection getRuntimeInfoSection() {
      RuntimeInfoSection section = RuntimeInfoSection.builder()
            .build();

      return section;
   }

   public static byte[] getScreenImage() {
      byte[] image = new byte[0];

      return image;
   }

   public static ScreenTicket getScreenTicket() {
      ScreenTicket ticket = null; // = ScreenTicket.builder();
//            .build();

      return ticket;
   }

   public static StartupSection getStartupSection() {
      StartupSection section = null; // = StartupSection.builder();
//            .build();

      return section;
   }

   public static Task modifyStartupSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static VirtualHardwareSection getVirtualHardwareSection() {
      VirtualHardwareSection section = VirtualHardwareSection.builder()
            .build();

      return section;
   }

   public static Task modifyVirtualHardwareSectionTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static RasdItem getVirtualHardwareSectionCpu() {
      RasdItem cpu = RasdItem.builder()
            .build();

      return cpu;
   }

   public static Task modifyVirtualHardwareSectionCpuTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static RasdItemsList getVirtualHardwareSectionDisks() {
      RasdItemsList disks = RasdItemsList.builder()
            .build();

      return disks;
   }

   public static Task modifyVirtualHardwareSectionDisksTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static RasdItemsList getVirtualHardwareSectionMedia() {
      RasdItemsList media = RasdItemsList.builder()
            .build();

      return media;
   }

   public static RasdItem getVirtualHardwareSectionMemory() {
      RasdItem memory = RasdItem.builder()
            .build();

      return memory;
   }

   public static Task modifyVirtualHardwareSectionMemoryTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static RasdItemsList getVirtualHardwareSectionNetworkCards() {
      RasdItemsList networkCards = RasdItemsList.builder()
            .build();

      return networkCards;
   }

   public static Task modifyVirtualHardwareSectionNetworkCardsTask() {
      Task task = Task.builder()
            .build();

      return task;
   }

   public static RasdItemsList getVirtualHardwareSectionSerialPorts() {
      RasdItemsList serialPorts = RasdItemsList.builder()
            .build();

      return serialPorts;
   }

   public static Task modifyVirtualHardwareSectionSerialPortsTask() {
      return task("id", "name", "description", "status", "operation", "operationName", "startTime");
   }

   /** Used by other methods to create a custom {@link Task} object. */
   private static Task task(String taskId, String name, String description, String status, String operation, String operationName, String startTime) {
      Task task = Task.builder()
            .error(Error.builder().build())
            .org(Reference.builder().build())
            .owner(Reference.builder().build())
            .user(Reference.builder().build())
            .params(null)
            .progress(0)
            .status(status)
            .operation(operation)
            .operationName(operationName)
            .startTime(dateService.iso8601DateParse(startTime))
            .endTime(null)
            .expiryTime(null)
            .tasks(Sets.<Task>newLinkedHashSet())
            .description(description)
            .name(name)
            .id("urn:vcloud:" + taskId)
            .href(URI.create(endpoint + "/task/" + taskId))
            .links(Sets.<Link>newLinkedHashSet())
            .build();

      return task;
   }
}
