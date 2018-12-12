/*
 * Copyright © 2013-2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.db.entitymanager;

import org.camunda.bpm.engine.impl.db.DbEntity;
import org.camunda.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.camunda.bpm.engine.impl.db.entitymanager.operation.DbOperationType;

/**
 * Allows registering a listener which is notified when an
 * {@link DbOperationType#UPDATE} or {@link DbOperationType#DELETE}
 * could not be performed.
 *
 * @author Daniel Meyer
 *
 */
public interface OptimisticLockingListener {

  /**
   * The type of the entity for which this listener should be notified.
   * If the implementation returns 'null', the listener is notified for all
   * entity types.
   *
   * @return the entity type for which the listener should be notified.
   */
  Class<? extends DbEntity> getEntityType();

  /**
   * Signifies that an operation failed due to optimistic locking.
   *
   * @param operation the failed operation.
   */
  void failedOperation(DbOperation operation);

}
