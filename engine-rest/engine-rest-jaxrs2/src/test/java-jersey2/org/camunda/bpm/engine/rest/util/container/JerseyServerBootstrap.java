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
package org.camunda.bpm.engine.rest.util.container;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class JerseyServerBootstrap extends EmbeddedServerBootstrap {

  private HttpServer server;

  public JerseyServerBootstrap(Application application) {
    setupServer(application);
  }

  @Override
  public void start() {
    try {
      server.start();
    } catch (IOException e) {
      throw new ServerBootstrapException(e);
    }
  }

  private void setupServer(Application application) {
    ResourceConfig rc = ResourceConfig.forApplication(application);

    Properties serverProperties = readProperties();
    int port = Integer.parseInt(serverProperties.getProperty(PORT_PROPERTY));
    URI serverUri = UriBuilder.fromPath(ROOT_RESOURCE_PATH).scheme("http").host("localhost").port(port).build();

    final HttpServer grizzlyServer = GrizzlyHttpServerFactory.createHttpServer(serverUri, rc);
    try {
      grizzlyServer.start();
    } catch (IOException e) {
      e.printStackTrace();
    }

    server = grizzlyServer;

  }

  @Override
  public void stop() {
    server.stop();
  }
}
