/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.test.jobexecutor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.batch.history.HistoricBatch;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.api.runtime.migration.MigrationTestRule;
import org.camunda.bpm.engine.test.api.runtime.migration.batch.BatchMigrationHelper;
import org.camunda.bpm.engine.test.util.ProvidedProcessEngineRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

public class JobExecutorBatchTest {

  protected ProcessEngineRule engineRule = new ProvidedProcessEngineRule();
  protected MigrationTestRule migrationRule = new MigrationTestRule(engineRule);
  protected BatchMigrationHelper helper = new BatchMigrationHelper(engineRule, migrationRule);

  @Rule
  public RuleChain ruleChain = RuleChain.outerRule(engineRule).around(migrationRule);
  public CountingJobExecutor jobExecutor;
  protected JobExecutor defaultJobExecutor;

  @Before
  public void replaceJobExecutor() throws Exception {
    ProcessEngineConfigurationImpl processEngineConfiguration = engineRule.getProcessEngineConfiguration();
    defaultJobExecutor = processEngineConfiguration.getJobExecutor();
    jobExecutor = new CountingJobExecutor();
    processEngineConfiguration.setJobExecutor(jobExecutor);
  }

  @After
  public void resetJobExecutor() {
    engineRule.getProcessEngineConfiguration()
      .setJobExecutor(defaultJobExecutor);
  }

  @After
  public void removeBatches() {
    ManagementService managementService = engineRule.getManagementService();
    HistoryService historyService = engineRule.getHistoryService();

    for (Batch batch : managementService.createBatchQuery().list()) {
      managementService.deleteBatch(batch.getId(), true);
    }
    for (HistoricBatch historicBatch : historyService.createHistoricBatchQuery().list()) {
      historyService.deleteHistoricBatch(historicBatch.getId());
    }
  }

  @Test
  public void testJobExecutorHintedOnBatchCreation() {
    // given
    jobExecutor.startRecord();

    // when a batch is created
    Batch batch = helper.migrateProcessInstancesAsync(2);

    // then the job executor is hinted for the seed job
    assertEquals(1, jobExecutor.getJobsAdded());
  }

  @Test
  public void testJobExecutorHintedSeedJobExecution() {
    // given
    Batch batch = helper.migrateProcessInstancesAsync(13);
    jobExecutor.startRecord();

    // when the seed job is executed
    helper.executeSeedJob(batch);

    // then the job executor is hinted for the seed job and 10 execution jobs
    assertEquals(11, jobExecutor.getJobsAdded());
  }

  @Test
  public void testJobExecutorHintedSeedJobCompletion() {
    // given
    Batch batch = helper.migrateProcessInstancesAsync(3);
    jobExecutor.startRecord();

    // when the seed job is executed
    helper.executeSeedJob(batch);

    // then the job executor is hinted for the monitor job and 3 execution jobs
    assertEquals(4, jobExecutor.getJobsAdded());
  }

  public class CountingJobExecutor extends JobExecutor {

    public boolean record = false;
    public long jobsAdded = 0;

    @Override
    public boolean isActive() {
      return true;
    }

    protected void startExecutingJobs() {
      // do nothing
    }

    protected void stopExecutingJobs() {
      // do nothing
    }

    public void executeJobs(List<String> jobIds, ProcessEngineImpl processEngine) {
      // do nothing
    }

    public void startRecord() {
      resetJobsAdded();
      record = true;
    }

    public void jobWasAdded() {
      if (record) {
        jobsAdded++;
      }
    }

    public long getJobsAdded() {
      return jobsAdded;
    }

    public void resetJobsAdded() {
      jobsAdded = 0;
    }

  }

}
