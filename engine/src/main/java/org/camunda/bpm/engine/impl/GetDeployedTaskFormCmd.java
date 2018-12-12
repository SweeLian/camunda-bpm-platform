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

import java.util.concurrent.Callable;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.impl.cfg.CommandChecker;
import org.camunda.bpm.engine.impl.cmd.AbstractGetDeployedFormCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskFormCmd;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.util.EnsureUtil;

/**
 * 
 * @author Anna Pazola
 *
 */
public class GetDeployedTaskFormCmd extends AbstractGetDeployedFormCmd {

  protected String taskId;

  public GetDeployedTaskFormCmd(String taskId) {
    EnsureUtil.ensureNotNull(BadUserRequestException.class, "Task id cannot be null", "taskId", taskId);
    this.taskId = taskId;
  }

  @Override
  protected FormData getFormData(final CommandContext commandContext) {
    return commandContext.runWithoutAuthorization(new Callable<FormData>() {

      @Override
      public FormData call() throws Exception {
        return new GetTaskFormCmd(taskId).execute(commandContext);
      }
    });
  }

  @Override
  protected void checkAuthorization(CommandContext commandContext) {
    TaskEntity taskEntity = commandContext.getTaskManager().findTaskById(taskId);
    for (CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
      checker.checkReadTask(taskEntity);
    }
  }

}
