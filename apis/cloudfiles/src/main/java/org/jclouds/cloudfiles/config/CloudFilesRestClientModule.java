/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.cloudfiles.config;

import static org.jclouds.util.Suppliers2.getLastValueInMap;

import java.net.URI;

import javax.inject.Singleton;

import org.jclouds.cloudfiles.CDNManagement;
import org.jclouds.cloudfiles.CloudFilesAsyncClient;
import org.jclouds.cloudfiles.CloudFilesClient;
import org.jclouds.location.suppliers.RegionIdToURISupplier;
import org.jclouds.openstack.keystone.v1_1.config.AuthenticationServiceModule;
import org.jclouds.openstack.swift.CommonSwiftAsyncClient;
import org.jclouds.openstack.swift.CommonSwiftClient;
import org.jclouds.openstack.swift.Storage;
import org.jclouds.openstack.swift.config.SwiftRestClientModule;
import org.jclouds.rest.ConfiguresRestClient;
import org.jclouds.rest.annotations.ApiVersion;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.inject.Provides;
import com.google.inject.Scopes;

/**
 * 
 * @author Adrian Cole
 */
@ConfiguresRestClient
public class CloudFilesRestClientModule extends SwiftRestClientModule<CloudFilesClient, CloudFilesAsyncClient> {
   public CloudFilesRestClientModule() {
      super(TypeToken.of(CloudFilesClient.class), TypeToken.of(CloudFilesAsyncClient.class), ImmutableMap
               .<Class<?>, Class<?>> of());
   }

   protected void bindResolvedClientsToCommonSwift() {
      bind(CommonSwiftClient.class).to(CloudFilesClient.class).in(Scopes.SINGLETON);
      bind(CommonSwiftAsyncClient.class).to(CloudFilesAsyncClient.class).in(Scopes.SINGLETON);
   }

   public static class StorageAndCDNManagementEndpointModule extends AuthenticationServiceModule {
      @Provides
      @Singleton
      @CDNManagement
      protected Supplier<URI> provideCDNUrl(RegionIdToURISupplier.Factory factory, @ApiVersion String apiVersion) {
         return getLastValueInMap(factory.createForApiTypeAndVersion("cloudFilesCDN", apiVersion));
      }

      @Provides
      @Singleton
      @Storage
      protected Supplier<URI> provideStorageUrl(RegionIdToURISupplier.Factory factory, @ApiVersion String apiVersion) {
         return getLastValueInMap(factory.createForApiTypeAndVersion("cloudFiles", apiVersion));
      }
   }

}
