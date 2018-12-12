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
package org.camunda.bpm.engine.test.standalone.variablescope;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;

/**
 * @author Roman Smirnov
 * @author Christian Lipphardt
 *
 */
public class VariableScopeTest extends PluggableProcessEngineTestCase {

  /**
   * A testcase to produce and fix issue ACT-862.
   */
  @Deployment
  public void testVariableNamesScope() {

    // After starting the process, the task in the subprocess should be active
    Map<String, Object> varMap = new HashMap<String, Object>();
    varMap.put("test", "test");
    varMap.put("helloWorld", "helloWorld");
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("simpleSubProcess", varMap);
    Task subProcessTask = taskService.createTaskQuery()
        .processInstanceId(pi.getId())
        .singleResult();
    runtimeService.setVariableLocal(pi.getProcessInstanceId(), "mainProcessLocalVariable", "Hello World");

    assertEquals("Task in subprocess", subProcessTask.getName());

    runtimeService.setVariableLocal(subProcessTask.getExecutionId(), "subProcessLocalVariable", "Hello SubProcess");

    // Returns a set of local variablenames of pi
    List<String> result = processEngineConfiguration.
            getCommandExecutorTxRequired().
            execute(new GetVariableNamesCommand(pi.getProcessInstanceId(), true));

    // pi contains local the variablenames "test", "helloWorld" and "mainProcessLocalVariable" but not "subProcessLocalVariable"
    assertTrue(result.contains("test"));
    assertTrue(result.contains("helloWorld"));
    assertTrue(result.contains("mainProcessLocalVariable"));
    assertFalse(result.contains("subProcessLocalVariable"));

    // Returns a set of global variablenames of pi
    result = processEngineConfiguration.
            getCommandExecutorTxRequired().
            execute(new GetVariableNamesCommand(pi.getProcessInstanceId(), false));

    // pi contains global the variablenames "test", "helloWorld" and "mainProcessLocalVariable" but not "subProcessLocalVariable"
    assertTrue(result.contains("test"));
    assertTrue(result.contains("mainProcessLocalVariable"));
    assertTrue(result.contains("helloWorld"));
    assertFalse(result.contains("subProcessLocalVariable"));

    // Returns a set of local variablenames of subProcessTask execution
    result = processEngineConfiguration.
            getCommandExecutorTxRequired().
            execute(new GetVariableNamesCommand(subProcessTask.getExecutionId(), true));

    // subProcessTask execution contains local the variablenames "test", "subProcessLocalVariable" but not "helloWorld" and "mainProcessLocalVariable"
    assertTrue(result.contains("test")); // the variable "test" was set locally by SetLocalVariableTask
    assertTrue(result.contains("subProcessLocalVariable"));
    assertFalse(result.contains("helloWorld"));
    assertFalse(result.contains("mainProcessLocalVariable"));

    // Returns a set of global variablenames of subProcessTask execution
    result = processEngineConfiguration.
            getCommandExecutorTxRequired().
            execute(new GetVariableNamesCommand(subProcessTask.getExecutionId(), false));

    // subProcessTask execution contains global all defined variablenames
    assertTrue(result.contains("test")); // the variable "test" was set locally by SetLocalVariableTask
    assertTrue(result.contains("subProcessLocalVariable"));
    assertTrue(result.contains("helloWorld"));
    assertTrue(result.contains("mainProcessLocalVariable"));

    taskService.complete(subProcessTask.getId());
  }

  /**
   * A command to get the names of the variables
   * @author Roman Smirnov
   * @author Christian Lipphardt
   */
  private class GetVariableNamesCommand implements Command<List<String>> {

    private String executionId;
    private boolean isLocal;


    public GetVariableNamesCommand(String executionId, boolean isLocal) {
     this.executionId = executionId;
     this.isLocal = isLocal;
    }

    public List<String> execute(CommandContext commandContext) {
      ensureNotNull("executionId", executionId);

      ExecutionEntity execution = commandContext
        .getExecutionManager()
        .findExecutionById(executionId);

      ensureNotNull("execution " + executionId + " doesn't exist", "execution", execution);

      List<String> executionVariables;
      if (isLocal) {
        executionVariables = new ArrayList<String>(execution.getVariableNamesLocal());
      } else {
        executionVariables = new ArrayList<String>(execution.getVariableNames());
      }

      return executionVariables;
    }

  }
}
