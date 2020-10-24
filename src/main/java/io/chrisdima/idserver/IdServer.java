package io.chrisdima.idserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IdServer extends AbstractVerticle {
  ID id;

  @Override
  public void start(Future<Void> future) {
    this.id = getIDGenerator((int)Thread.currentThread().getId());

    Router router = Router.router(vertx);
    router.get("/api/id").handler(this::getID);

    vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(
            // Retrieve the port from the configuration,
            // default to 8080.
            config().getInteger("http.port", 8080),
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
    ID id = null;
    try {
      id = new ID(getMachineId(), workerID);
    } catch (Exception e) {
      System.out.println(e);
    }
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
