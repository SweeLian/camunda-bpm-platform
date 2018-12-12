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
package org.camunda.bpm.qa.rolling.update;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.qa.rolling.update.scenarios.DeploymentWhichShouldBeDeletedScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.authorization.AuthorizationScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.callactivity.ProcessWithCallActivityScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.eventSubProcess.ProcessWithEventSubProcessScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.externalTask.ProcessWithExternalTaskScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.mulltiInstance.ProcessWithMultiInstanceCallActivityScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.task.ProcessWithAsyncServiceTaskScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.task.ProcessWithParallelGatewayAndServiceTaskScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.task.ProcessWithParallelGatewayScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.task.ProcessWithUserTaskAndTimerScenario;
import org.camunda.bpm.qa.rolling.update.scenarios.task.ProcessWithUserTaskScenario;
import org.camunda.bpm.qa.upgrade.ScenarioRunner;

/**
 * Sets up scenarios for rolling updates.
 *
 * @author Thorben Lindhauer
 * @author Christopher Zell
 */
public class TestFixture {

  public static final String DEFAULT_TAG = RollingUpdateConstants.OLD_ENGINE_TAG;
  public static String currentFixtureTag;

  public TestFixture(ProcessEngine processEngine) {
  }

  public static void main(String[] args) {
    String tag = DEFAULT_TAG;
    if (args.length > 0) {
      tag = args[0];
    }
    currentFixtureTag = tag;

    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
      .createProcessEngineConfigurationFromResource("camunda.cfg.xml");
    ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

    // register test scenarios
    ScenarioRunner runner = new ScenarioRunner(processEngine, tag);
    // compensation
    //rolling upgrade test scenarios
    runner.setupScenarios(ProcessWithUserTaskScenario.class);
    runner.setupScenarios(ProcessWithAsyncServiceTaskScenario.class);
    runner.setupScenarios(ProcessWithUserTaskAndTimerScenario.class);
    runner.setupScenarios(DeploymentWhichShouldBeDeletedScenario.class);
    runner.setupScenarios(ProcessWithParallelGatewayScenario.class);
    runner.setupScenarios(ProcessWithParallelGatewayAndServiceTaskScenario.class);
    runner.setupScenarios(ProcessWithCallActivityScenario.class);
    runner.setupScenarios(ProcessWithMultiInstanceCallActivityScenario.class);
    runner.setupScenarios(ProcessWithExternalTaskScenario.class);
    runner.setupScenarios(ProcessWithEventSubProcessScenario.class);

    processEngine.close();

    processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
        .createProcessEngineConfigurationFromResource("camunda.auth.cfg.xml");
    processEngine = processEngineConfiguration.buildProcessEngine();

    // register test auth scenarios
    runner = new ScenarioRunner(processEngine, tag);

    runner.setupScenarios(AuthorizationScenario.class);

    processEngine.close();
  }
}
