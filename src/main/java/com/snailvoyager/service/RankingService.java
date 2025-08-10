package com.snailvoyager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RankingService {

    public static final String LEADERBOARD_KEY = "leaderBoard";
    private final StringRedisTemplate redisTemplate;

    public boolean setUserScore(String userId, int score) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.add(LEADERBOARD_KEY, userId, score);
        
        return true;
    }

    public Long getUserRanking(String userId) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        return zSetOps.reverseRank(LEADERBOARD_KEY, userId);
    }

    public List<String> getTopRank(int limit) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<String> rank = zSetOps.reverseRange(LEADERBOARD_KEY, 0, limit - 1);

        return new ArrayList<>(rank);
    }
}
