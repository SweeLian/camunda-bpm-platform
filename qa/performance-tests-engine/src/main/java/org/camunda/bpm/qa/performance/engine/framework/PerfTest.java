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
package org.camunda.bpm.qa.performance.engine.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A performance test.</p>
 *
 * <p>A performance test is composed of a sequence of steps. Each steps will
 * move the performance test forward and may be scheduled using a tread pool</p>
 *
 * @author Daniel Meyer
 *
 */
public class PerfTest {

  /** the individual steps that make up the performance test */
  protected List<PerfTestStep> steps = new ArrayList<PerfTestStep>();

  public void addStep(PerfTestStep step) {
    if(steps.isEmpty()) {
      // this is the first step
      steps.add(step);
    } else {
      // link the step to the last step
      PerfTestStep lastStep = steps.get(steps.size() -1);
      lastStep.setNextStep(step);
      steps.add(step);
    }
  }

  public PerfTestStep getFirstStep() {
    return steps.get(0);
  }

}
