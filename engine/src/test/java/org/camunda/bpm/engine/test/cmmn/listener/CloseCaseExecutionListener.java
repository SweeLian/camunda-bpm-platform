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
package org.camunda.bpm.engine.test.cmmn.listener;

import java.io.Serializable;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;

/**
 * @author Roman Smirnov
 *
 */
public class CloseCaseExecutionListener implements CaseExecutionListener, Serializable {

  private static final long serialVersionUID = 1L;

  protected static String EVENT;
  protected static int COUNTER = 0;
  protected static String ON_CASE_EXECUTION_ID;

  public void notify(DelegateCaseExecution caseExecution) throws Exception {
    EVENT = caseExecution.getEventName();
    COUNTER = COUNTER + 1;
    ON_CASE_EXECUTION_ID = caseExecution.getId();
  }

  public static void clear() {
    EVENT = null;
    COUNTER = 0;
    ON_CASE_EXECUTION_ID = null;
  }

}
