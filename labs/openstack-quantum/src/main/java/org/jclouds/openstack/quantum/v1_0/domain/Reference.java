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
package org.jclouds.openstack.quantum.v1_0.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * A wrapper around an id in the quantum api
 *
 * @author Adam Lowe
 * @see <a href="http://docs.openstack.org/api/openstack-network/1.0/content/Networks.html">api doc</a>
 */
public class Reference {

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromReference(this);
   }

   public static abstract class Builder<T extends Builder<T>>  {
      protected abstract T self();

      private String id;

      /**
       * @see org.jclouds.openstack.quantum.v1_0.domain.Reference#getId()
       */
      public T id(String id) {
         this.id = id;
         return self();
      }

      public Reference build() {
         return new Reference(this);
      }

      public T fromReference(Reference in) {
         return this.id(in.getId());
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }

   private final String id;

   protected Reference(Builder<?> builder) {
      this.id = checkNotNull(builder.id, "id");
   }

   protected Reference() {
      // for GSON
      this.id = null;
   }

   /**
    */
   public String getId() {
      return this.id;
   }


   @Override
   public int hashCode() {
      return Objects.hashCode(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Reference that = Reference.class.cast(obj);
      return Objects.equal(this.id, that.id);
   }

   protected ToStringHelper string() {
      return Objects.toStringHelper("").add("id", id);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}