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
package org.camunda.bpm.qa.rolling.update.multiInstance;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.qa.rolling.update.AbstractRollingUpdateTestCase;
import org.camunda.bpm.qa.upgrade.ScenarioUnderTest;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author Christopher Zell <christopher.zell@camunda.com>
 */
@ScenarioUnderTest("ProcessWithMultiInstanceCallActivityScenario")
public class CompleteProcessWithMultiInstanceCallActivityTest extends AbstractRollingUpdateTestCase {

  @Test
  @ScenarioUnderTest("init.1")
  public void testCompleteProcessWithCallActivity() {
    //given process with user task before multi-instance call activity
    ProcessInstance processInstance = rule.processInstance();
    TaskQuery taskQuery = rule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId());
    Task taskBeforeSubProcess = taskQuery.taskName("Task before multi-instance").singleResult();
    assertNotNull(taskBeforeSubProcess);

    //when the task before is complete the process leads to calling the multi-instance subprocess
    rule.getTaskService().complete(taskBeforeSubProcess.getId());

    Task taskAfterSubProcess = taskQuery.taskName("Task after multi-instance").singleResult();
    assertNotNull(taskAfterSubProcess);

    //after completing the after task the process instance ends
    rule.getTaskService().complete(taskAfterSubProcess.getId());
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.complete.one.1")
  public void testCompleteProcessWithCallActivityAndOneCompletedTask() {
    //given process after multi-instance callactivity
    ProcessInstance processInstance = rule.processInstance();
    TaskQuery taskQuery = rule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId());
    Task taskAfterSubProcess = taskQuery.taskName("Task after multi-instance").singleResult();
    assertNotNull(taskAfterSubProcess);

    // Completing this task end the process instance
    rule.getTaskService().complete(taskAfterSubProcess.getId());
    rule.assertScenarioEnded();
  }

}
