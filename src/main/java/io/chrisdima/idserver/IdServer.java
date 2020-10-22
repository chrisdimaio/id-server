package io.chrisdima.idserver;

import java.util.HashMap;
import java.util.HashSet;

public class IdServer {
  private static HashMap<String, Integer> test = new HashMap<>();
  public static void main(String[] args) throws Exception {
    ID id0 = new ID(0);
    ID id1 = new ID(1);
    ID id2 = new ID(2);
    ID id3 = new ID(3);

    while(true) {
      test(id0.generate());
      test(id1.generate());
      test(id2.generate());
      test(id3.generate());
      System.out.println("IDs in map: " + test.size());
    }
  }

  static void test(String id) throws Exception {
    if(IdServer.test.containsKey(id)) {
      throw new Exception("COLLISION...");
    }
    IdServer.test.put(id, 0);
  }
}
