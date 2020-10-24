package io.chrisdima.idserver;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

public class IdServer {
  private static HashMap<String, Integer> test = new HashMap<>();
  public static void main(String[] args) throws Exception {
    ID id = new ID(getMachineId(), 0);
    ID id1 = new ID(getMachineId(), 1);

    String i0;
    String i1;
    long counter = 0;
    while(true) {
      i0 = id.generate();
      i1 = id1.generate();
      if(i0.equals(i1)) {
        System.out.println("Collision between: " + i0 + " and " + i1);
        System.exit(0);
      }
      if(counter++ % 2000000 == 0){
        System.out.print(".");
      }
      if(counter % 100000000 == 0){
        System.out.print(counter / 1000000 + " mil");
        System.out.println();
      }
    }
  }

  /**
   * Machine ID is a hash of the MAC address of the first NIC with a MAC.
   * @return int representing the machine's id, -1 if no
   * @throws SocketException
   */
  private static int getMachineId() throws SocketException {
    NetworkInterface networkInterface;
    while(NetworkInterface.getNetworkInterfaces().hasMoreElements()) {
      networkInterface = NetworkInterface.getNetworkInterfaces().nextElement();
      if(networkInterface.getHardwareAddress() != null) {
        return Arrays.hashCode(networkInterface.getHardwareAddress());
      }
    }
    throw new IllegalStateException("System doesn't have a network interface with a MAC.");
  }

  static void test(String id) throws Exception {
    if(IdServer.test.containsKey(id)) {
      throw new Exception("COLLISION...");
    }
    IdServer.test.put(id, 0);
  }
}
