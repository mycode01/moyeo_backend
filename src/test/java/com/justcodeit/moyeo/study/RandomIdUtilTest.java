package com.justcodeit.moyeo.study;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class RandomIdUtilTest {

  @Test
  void testNext() {
    var randomIdUtil = new RandomIdUtil();
    String result = randomIdUtil.next();
    Assertions.assertNotNull(result);

    int times = 5000;
    Set<String> set = new HashSet<String>();
    for (int i = 0; i < times; i++) {
      set.add(randomIdUtil.next());
    }
    Assertions.assertEquals(times, set.size());
  }

  @Test
  void multiThreadTest() {
    final var u = new RandomIdUtil();
    Supplier<List<String>> testCallback = () -> {
      var l = new ArrayList<String>();
      for (int i = 0; i < 10000; i++) {
        String id = u.next();
        l.add(id);
//        log.debug(id);
      }
      return l;
    };
    var executor = Executors.newFixedThreadPool(3);
    CompletableFuture<List<String>> f1 = CompletableFuture.supplyAsync(testCallback, executor);
    CompletableFuture<List<String>> f2 = CompletableFuture.supplyAsync(testCallback, executor);
    CompletableFuture<List<String>> f3 = CompletableFuture.supplyAsync(testCallback, executor);
    CompletableFuture<List<String>> f4 = CompletableFuture.supplyAsync(testCallback, executor);
    CompletableFuture.allOf(f1, f2, f3, f4);
    log.debug("all works over");
    var resultList = Stream.of(f1, f2, f3, f4).map(CompletableFuture::join).flatMap(List::stream)
        .collect(Collectors.toList());
    var resultSet = new HashSet<>(resultList);

    Assertions.assertEquals(10000*4, resultSet.size());

  }
}