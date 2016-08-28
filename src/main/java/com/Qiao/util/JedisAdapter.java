package com.Qiao.util;

import com.Qiao.model.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

/**
 * Created by white and black on 2016/8/22.
 */
@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger= Logger.getLogger(JedisAdapter.class);
    private JedisPool pool;

    public static void print(int index, Object obj){
        System.out.println(String.format("%d:%s",index,obj.toString()));
    }

    public static void main(String[] args){
        Jedis jedis=new Jedis("redis://localhost:6379/9");
        jedis.flushDB();
        //get,set
        jedis.set("hello","world");
        jedis.rename("hello","newHello");
        jedis.setex("hello2",10,"world");
        //incr,decr,incrBy,decrBy
        jedis.set("pv","100");
        jedis.incr("pv");
        jedis.incrBy("pv",100);
        jedis.decrBy("pv",10);
        jedis.keys("*");
        //List
        String listName="list";
        for(int i=0;i<10;i++){
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        //jedis.llen(listName);
        print(4,jedis.lrange(listName,0,12));
        print(4,jedis.lrange(listName,0,3));
        print(4,jedis.lpop(listName));
        print(4,jedis.llen(listName));
        print(4,jedis.lindex(listName,4));
        print(4,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(4,jedis.linsert(listName,BinaryClient.LIST_POSITION.BEFORE,"a4","bb"));
        print(4,jedis.lrange(listName,0,12));
        //hash
        String userKey="userxx";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","10003393");
        print(5,jedis.hget(userKey,"name"));
        print(5,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(5,jedis.hgetAll(userKey));
        print(5,jedis.hexists(userKey,"email"));
        print(5,jedis.hexists(userKey,"age"));
        print(5,jedis.hkeys(userKey));
        print(5,jedis.hvals(userKey));
        //set
        String likeKey1="commentLike1";
        String likeKey2="commentLike2";
        for (int i=0;i<10;++i)
        {
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));
        }
        print(6,jedis.smembers(likeKey1));
        print(6,jedis.smembers(likeKey2));
        print(6,jedis.sunion(likeKey1,likeKey2));
        print(6,jedis.sdiff(likeKey1,likeKey2));
        print(6,jedis.sinter(likeKey1,likeKey2));
        print(6,jedis.sismember(likeKey1,"12"));
        print(6,jedis.sismember(likeKey2,"16"));
        jedis.srem(likeKey1,"5");
        print(6,jedis.smembers(likeKey1));
        jedis.srem(likeKey1,"9");

        print(6,jedis.scard(likeKey1));

        //z
        String rankKey="rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,90,"Lee");
        jedis.zadd(rankKey,75,"Lucy");
        jedis.zadd(rankKey,80,"Mei");
        print(9,jedis.zcard(rankKey));
        print(9,jedis.zcount(rankKey,61,100));
        print(9,jedis.zscore(rankKey,"Lucy"));
        jedis.zincrby(rankKey,2,"Luc");
        jedis.zincrby(rankKey,18,"Luc");
        print(9,jedis.zscore(rankKey,"Luc"));
        print(9,jedis.zrange(rankKey,0,100));
        print(9,jedis.zrange(rankKey,0,20));
        print(9,jedis.zrange(rankKey,0,2));
        print(9,jedis.zrevrange(rankKey,0,3));
        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"60","100")){
            print(10,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }
        print(11,jedis.zrank(rankKey,"Ben"));
        print(11,jedis.zrevrank(rankKey,"Ben"));


        String setKey="zset";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");
        print(12,jedis.zlexcount(setKey,"-","+"));
        print(12,jedis.zlexcount(setKey,"(b","[d"));
        print(12,jedis.zlexcount(setKey,"[b","[d"));
        jedis.zrem(setKey,"b");
        print(13,jedis.zrange(setKey,0,10));
        jedis.zremrangeByLex(setKey,"(c","+");
        print(13,jedis.zrange(setKey,0,2));


        User user=new User();
        user.setName("bb");
        user.setPassword("bb");
        user.setHeadUrl("E:\\wenda2\\src\\main\\resources\\static\\images\\res\\a.png");
        user.setSalt("salt");
        user.setId(1);
        print(14, JSONObject.toJSONString(user));
        jedis.set("user1",JSONObject.toJSONString(user));
        String value=jedis.get("user1");
        User user2= JSON.parseObject(value,User.class);
        print(15,user2);
//
//        JedisPool pool=new JedisPool();
//        for(int i=0;i<100;++i){
//            Jedis j=pool.getResource();
//            print(22,j.get("pv"));
//            j.close();
//        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool =new JedisPool("redis://localhost:6379/10");
    }
    public long sadd(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    public long srem(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
        return false;
    }
    public List<String> brpop(int timeout, String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
}
