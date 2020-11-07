package io.chrisdima.idserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Map;

public class IDServerVerticle extends AbstractVerticle {
  private static final int DEFAULT_HTTP_PORT = 8080;
  private static final String DEFAULT_ROUTE = "/api/id";

  private ID id;

  @Override
  public void start(Future<Void> future) {
    id = getIDGenerator((int)Thread.currentThread().getId());

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

  private ID getIDGenerator(int threadID) {
    Logger logger = LoggerFactory.getLogger(IDServerVerticle.class);
    ID id = null;
    int machineID = -1;
    try {
      machineID = getMachineId();
      id = new ID(machineID, threadID);
    } catch (Exception e) {
      logger.error(e);
    }
    logger.info("machineID: " + machineID);
    logger.info(id);
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
