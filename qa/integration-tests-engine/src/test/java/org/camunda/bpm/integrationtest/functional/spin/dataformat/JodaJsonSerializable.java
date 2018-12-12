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
package org.camunda.bpm.integrationtest.functional.spin.dataformat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author Thorben Lindhauer
 *
 */
public class JodaJsonSerializable {

  public static final long ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
  protected static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime();

  protected DateTime dateProperty;

  public JodaJsonSerializable(DateTime dateTime) {
    this.dateProperty = dateTime;
  }

  public DateTime getDateProperty() {
    return dateProperty;
  }

  public void setDateProperty(DateTime dateTime) {
    this.dateProperty = dateTime;
  }

  /**
   * Serializes the value as milliseconds
   */
  public String toExpectedJsonString() {
    StringBuilder jsonBuilder = new StringBuilder();

    jsonBuilder.append("{\"dateProperty\":\"");
    jsonBuilder.append(dateProperty.withZone(DateTimeZone.UTC).toString(DATE_TIME_FORMATTER));
    jsonBuilder.append("\"}");

    return jsonBuilder.toString();
  }
}
