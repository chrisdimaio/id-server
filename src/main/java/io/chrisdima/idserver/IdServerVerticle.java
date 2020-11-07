package io.chrisdima.idserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Map;

public class IdServerVerticle extends AbstractVerticle {
  private static final int DEFAULT_HTTP_PORT = 8080;
  private static final String DEFAULT_ROUTE = "/api/id";

  private final ID id = getIDGenerator((int)Thread.currentThread().getId());

  @Override
  public void start(Future<Void> future) {
    Logger logger = LoggerFactory.getLogger(IdServerVerticle.class);
    Router router = Router.router(vertx);
    router.get(config().getString("route", DEFAULT_ROUTE)).handler(this::getID);

    vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(
            config().getInteger("http.port", DEFAULT_HTTP_PORT),
            result -> {
              if (result.succeeded()) {
                future.complete();
              } else {
                future.fail(result.cause());
              }
            }
        );
  }

  private void getID(RoutingContext routingContext) {
    routingContext.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(Map.of("id", id.generate())));
  }

  private ID getIDGenerator(int workerID) {
    Logger logger = LoggerFactory.getLogger(IdServerVerticle.class);
    ID id = null;
    int machineID = -1;
    try {
      machineID = getMachineId();
      id = new ID(machineID, workerID);
    } catch (Exception e) {
      logger.error(e);
    }
    logger.debug(Thread.currentThread().getName() + "> machineID: " + machineID);
    logger.debug(Thread.currentThread().getName() + ">" + id);
    return id;
  }

  /**
   * Machine ID is a hash of the MAC address of the first NIC with a MAC.
   * @return int representing the machine's id, -1 if no
   * @throws SocketException
   */
  private int getMachineId() throws SocketException {
    NetworkInterface networkInterface;
    while(NetworkInterface.getNetworkInterfaces().hasMoreElements()) {
      networkInterface = NetworkInterface.getNetworkInterfaces().nextElement();
      if(networkInterface.getHardwareAddress() != null) {
        return Arrays.hashCode(networkInterface.getHardwareAddress());
      }
    }
    throw new IllegalStateException("System doesn't have a network interface with a MAC.");
  }
}
