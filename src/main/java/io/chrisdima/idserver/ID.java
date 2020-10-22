package io.chrisdima.idserver;

import java.nio.charset.IllegalCharsetNameException;

public class ID {
  private final int workerID;
  private int sequence;

  public ID(int workerID){
    this.workerID = get10BitWorkerID(workerID);
    this.sequence = -1;
  }

  public String generate() {
    return String.valueOf(
        get41BitTimestamp() << 22 | this.workerID << 12 | getSequence());
  }

  private int getSequence() {
    if (++this.sequence == 4096) {
      this.sequence = 0;
    }
    return this.sequence;
  }

  private int get10BitWorkerID(int workerID) {
    return workerID & 0x000003FF;
  }

  private long get41BitTimestamp() {
    return System.currentTimeMillis() & 0x00001FFFFFFFFFFL;
  }
}
