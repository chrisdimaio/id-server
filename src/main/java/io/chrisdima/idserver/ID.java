package io.chrisdima.idserver;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Arrays;

public class ID {
  private final int workerID;
  private int sequence;

  public ID(int machineID, int workerID){
    // Combining the worker id and machine id to make the worker id a little more unique.
    this.workerID = getWorkerID(Arrays.hashCode(new int[] {machineID, workerID}));
    this.sequence = -1;
  }

  public String generate() {
    return String.valueOf(
        getTimestamp() << 22 | this.workerID << 12 | getSequence());
  }

  private int getSequence() {
    if (++this.sequence == 4096) {
      this.sequence = 0;
    }
    return this.sequence;
  }

  private int getWorkerID(int workerID) {
    return workerID & 0x000003FF;
  }

  private long getTimestamp() {
    return System.currentTimeMillis() & 0x00001FFFFFFFFFFL;
  }
}
