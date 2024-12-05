package top.autuan.rank;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;

import java.util.Iterator;

/**
 * 排行榜 基于 redis
 */
public class RankComponent {

    private RedissonClient redissonClient;


    // 包含所有用户的排行榜
    Object all(String rankName){
        // zadd rank_name  10.0 user1 11.0 user2
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        Iterator<Object> iterator = rank.iterator();
        return iterator;
    };

    // 只包含前n名的排行榜
    Object top(String rankName,int n){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        Iterator<Object> iterator = rank.iterator(n);
        return iterator;
    }

    // 查询某一位用户的分数
    Object score(){
        // zscore rank_name username
        return null;
    }

    // 查询某一用户的排名
    Object rankNum(){
        // zrevrank rank_name username
return null;

    }

    // 更新某一用户的分值
    Object update(){
return null;
    }

    // 最近 n 天的排名
    Object rankByDate(){
return null;

    }

    // 复杂查询
    Object todo(){
return null;
    }
}
