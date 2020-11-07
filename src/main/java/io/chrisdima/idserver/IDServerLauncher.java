package io.chrisdima.idserver;

import io.vertx.core.Launcher;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class IDServerLauncher extends Launcher {
  private final Logger logger = LoggerFactory.getLogger( IDServerLauncher.class );

  public static void main(String[] args){
    new IDServerLauncher().dispatch(args);
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    if(deploymentOptions.getConfig().containsKey("instances")) {
      super.beforeDeployingVerticle(
          deploymentOptions
              .setInstances(deploymentOptions.getConfig().getInteger("instances")));
    }
    logger.info("Deployment Options: " + Json.encodePrettily(deploymentOptions.toJson()));
  }

}
