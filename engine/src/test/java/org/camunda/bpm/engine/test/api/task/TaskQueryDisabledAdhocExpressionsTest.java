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
package org.camunda.bpm.engine.test.api.task;

import java.util.Date;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.task.TaskQuery;

/**
 * @author Thorben Lindhauer
 *
 */
public class TaskQueryDisabledAdhocExpressionsTest extends PluggableProcessEngineTestCase {

  protected static final String EXPECTED_ADHOC_QUERY_FAILURE_MESSAGE = "Expressions are forbidden in adhoc queries. "
      + "This behavior can be toggled in the process engine configuration";
  public static final String STATE_MANIPULATING_EXPRESSION =
      "${''.getClass().forName('" + TaskQueryDisabledAdhocExpressionsTest.class.getName() + "').getField('MUTABLE_FIELD').setLong(null, 42)}";

  public static long MUTABLE_FIELD = 0;

  public void testDefaultSetting() {
    assertTrue(processEngineConfiguration.isEnableExpressionsInStoredQueries());
    assertFalse(processEngineConfiguration.isEnableExpressionsInAdhocQueries());
  }

  protected void setUp() throws Exception {
    MUTABLE_FIELD = 0;
  }

  public void testAdhocExpressionsFail() {
    executeAndValidateFailingQuery(taskService.createTaskQuery().dueAfterExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().dueBeforeExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().dueDateExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().followUpAfterExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().followUpBeforeExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().followUpBeforeOrNotExistentExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().followUpDateExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskAssigneeExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskAssigneeLikeExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskCandidateGroupExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskCandidateGroupInExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskCandidateUserExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskCreatedAfterExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskCreatedBeforeExpression(STATE_MANIPULATING_EXPRESSION));
    executeAndValidateFailingQuery(taskService.createTaskQuery().taskOwnerExpression(STATE_MANIPULATING_EXPRESSION));
  }

  public void testExtendStoredFilterByExpression() {

    // given a stored filter
    TaskQuery taskQuery = taskService.createTaskQuery().dueAfterExpression("${now()}");
    Filter filter = filterService.newTaskFilter("filter");
    filter.setQuery(taskQuery);
    filterService.saveFilter(filter);

    // it is possible to execute the stored query with an expression
    assertEquals(new Long(0), filterService.count(filter.getId()));
    assertEquals(0, filterService.list(filter.getId()).size());

    // but it is not possible to executed the filter with an extended query that uses expressions
    extendFilterAndValidateFailingQuery(filter, taskService.createTaskQuery().dueAfterExpression(STATE_MANIPULATING_EXPRESSION));

    // cleanup
    filterService.deleteFilter(filter.getId());
  }

  public void testExtendStoredFilterByScalar() {
    // given a stored filter
    TaskQuery taskQuery = taskService.createTaskQuery().dueAfterExpression("${now()}");
    Filter filter = filterService.newTaskFilter("filter");
    filter.setQuery(taskQuery);
    filterService.saveFilter(filter);

    // it is possible to execute the stored query with an expression
    assertEquals(new Long(0), filterService.count(filter.getId()));
    assertEquals(0, filterService.list(filter.getId()).size());

    // and it is possible to extend the filter query when not using an expression
    assertEquals(new Long(0), filterService.count(filter.getId(), taskService.createTaskQuery().dueAfter(new Date())));
    assertEquals(0, filterService.list(filter.getId(), taskService.createTaskQuery().dueAfter(new Date())).size());

    // cleanup
    filterService.deleteFilter(filter.getId());
  }

  protected boolean fieldIsUnchanged() {
    return MUTABLE_FIELD == 0;
  }

  protected void executeAndValidateFailingQuery(TaskQuery query) {
    try {
      query.list();
    } catch (BadUserRequestException e) {
      assertTextPresent(EXPECTED_ADHOC_QUERY_FAILURE_MESSAGE, e.getMessage());
    }

    assertTrue(fieldIsUnchanged());

    try {
      query.count();
    } catch (BadUserRequestException e) {
      assertTextPresent(EXPECTED_ADHOC_QUERY_FAILURE_MESSAGE, e.getMessage());
    }

    assertTrue(fieldIsUnchanged());
  }

  protected void extendFilterAndValidateFailingQuery(Filter filter, TaskQuery query) {
    try {
      filterService.list(filter.getId(), query);
    } catch (BadUserRequestException e) {
      assertTextPresent(EXPECTED_ADHOC_QUERY_FAILURE_MESSAGE, e.getMessage());
    }

    assertTrue(fieldIsUnchanged());

    try {
      filterService.count(filter.getId(), query);
    } catch (BadUserRequestException e) {
      assertTextPresent(EXPECTED_ADHOC_QUERY_FAILURE_MESSAGE, e.getMessage());
    }

    assertTrue(fieldIsUnchanged());
  }
}
