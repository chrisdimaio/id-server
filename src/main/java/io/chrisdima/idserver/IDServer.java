package io.chrisdima.idserver;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class IDServer {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DeploymentOptions options = new DeploymentOptions().setInstances(4);
    System.out.println(options.toJson());
    vertx.deployVerticle("io.chrisdima.idserver.IdServerVerticle", options);
  }
}
