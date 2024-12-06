package top.autuan.rank;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 排行榜 基于 redis
 */
public class RankComponent {

    private RedissonClient redissonClient;


    // 包含所有用户的排行榜
     // 包含所有用户的排行榜
    public Collection<ScoredEntry<Object>> all(String rankName){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        // 返回所有排行榜用户的得分及其排名
        // 0 到 -1 表示获取所有元素
        return rank.entryRange(0, -1);
    }

  // 只包含前n名的排行榜
    public Collection<ScoredEntry<Object>> top(String rankName, int n){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        // 获取前 n 名的得分及排名
        return rank.entryRange(0, n - 1);  // 0 到 n-1 是前 n 名
    }

      // 查询某一位用户的分数
    public Double score(String rankName, String user){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        // 返回用户的分数，若用户不在排行榜中则返回 null
        return rank.getScore(user);
    }

      // 查询某一用户的排名
    public Integer rankNum(String rankName, String user){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        // 查询该用户的排名，排名是从0开始的
          // 查询该用户的排名，排名是从0开始的
        // 返回正序排名，即从低分到高分
        return rank.rank(user);
    }

        // 更新某一用户的分值
    public void update(String rankName, String user, Integer score){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        // 更新用户的分数
        rank.add(score, user);
    }

     // 最近 n 天的排名（假设分数是根据时间来更新的，具体实现依赖业务需求）
     // 最近 n 天的排名（假设分数是根据时间来更新的，具体实现依赖业务需求）
    public Collection<ScoredEntry<Object>> rankByDate(String rankName, int dayNum){
        RScoredSortedSet<Object> rank = redissonClient.getScoredSortedSet(rankName);
        // 根据分数和时间的规则筛选，假设通过分数来推算日期区间
        // 需要根据你的业务逻辑定义分数与日期的映射方式
        Collection<ScoredEntry<Object>> entries = rank.entryRange(0, -1);  // 返回符合条件的排名
        return entries;
    }

     // 复杂查询，传入一个用户的自定义score解析程序
    public Object customizeQuery(String rankName,Runnable scoreCalculationTask){
        // 传入 Runnable 任务来计算并更新排行榜的分数
        // 在 Runnable 内部可以自定义分数计算的逻辑
        scoreCalculationTask.run();

        return all(rankName);
    }
}
