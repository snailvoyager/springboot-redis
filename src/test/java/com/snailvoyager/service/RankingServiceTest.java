package com.snailvoyager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class RankingServiceTest {

    @Autowired
    private RankingService rankingService;

    @Test
    void inMemorySortPerformance() {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i=0; i<1000000; i++) {
            int score = (int)(Math.random() * 1000000); // 0 ~ 999999
            list.add(score);
        }

        Instant before = Instant.now();
        Collections.sort(list); // nlogn
        Duration elapsed = Duration.between(before, Instant.now());
        System.out.println((elapsed.getNano() / 1000000) + " ms");
    }

    @Test
    void insertScore() {
        for (int i=0; i<1000000; i++) {
            int score = (int)(Math.random() * 1000000); // 0 ~ 999999
            String userId = "user_" + i;

            rankingService.setUserScore(userId, score);
        }
    }

    @Test
    void getRanks() {
        rankingService.getTopRank(1); // warm up

        Instant before = Instant.now();
        Long userRank = rankingService.getUserRanking("user_100");
        Duration elapsed = Duration.between(before, Instant.now());

        System.out.printf("Rank(%d) - Took %d ms%n", userRank, elapsed.getNano() / 1000000);

        before = Instant.now();
        List<String> topRanks = rankingService.getTopRank(10);
        elapsed = Duration.between(before, Instant.now());

        System.out.printf("Range - Took %d ms%n", elapsed.getNano() / 1000000);
    }

}