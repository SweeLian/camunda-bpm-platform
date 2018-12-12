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
package org.camunda.bpm.engine.impl;

import org.camunda.bpm.engine.management.JobDefinitionQuery;
import org.camunda.bpm.engine.query.QueryProperty;

/**
 * Contains the possible properties that can be used in a {@link JobDefinitionQuery}.
 *
 * @author roman.smirnov
 */
public interface JobDefinitionQueryProperty {

  public static final QueryProperty JOB_DEFINITION_ID = new QueryPropertyImpl("ID_");
  public static final QueryProperty ACTIVITY_ID = new QueryPropertyImpl("ACT_ID_");
  public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
  public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROC_DEF_KEY_");
  public static final QueryProperty JOB_TYPE = new QueryPropertyImpl("JOB_TYPE_");
  public static final QueryProperty JOB_CONFIGURATION = new QueryPropertyImpl("JOB_CONFIGURATION_");
  public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");

}
