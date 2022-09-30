package com.justcodeit.moyeo.study.common;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomIdUtil {
  private static final int counterMax = 256 * 256;
  private static final AtomicInteger intVal = new AtomicInteger(0);

  private static String generate() {
    final long uid = Instant.now().toEpochMilli() * counterMax + intVal.accumulateAndGet(1, (index, inc) -> (index + inc) % counterMax);

    return Long.toHexString(uid);
  }

  public String next() {
    return generate();
  }

}
