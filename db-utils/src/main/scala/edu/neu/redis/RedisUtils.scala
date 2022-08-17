package edu.neu.redis

import com.redis.RedisClientPool
import edu.neu.RSConfig

/**
 * Redis Utility:
 * Provide several redis operations
 */
case object RedisUtils {
  val clients = new RedisClientPool(RSConfig.redis_server, RSConfig.redis_port, database = 0)

  def hmset(key: String, kvs: Seq[Product2[Any, Any]]): Boolean = {
    clients.withClient(
      client => client.hmset(key, kvs)
    )
  }

  def hmget[K](key: String, fields: K*): Option[Map[K, String]] = {
    clients.withClient(
      client => client.hmget(key, fields: _*)
    )
  }

  def set(key: Any, value: Any): Boolean = {
    clients.withClient(
      client => client.set(key, value)
    )
  }

  def get(key: Any): Option[String] = {
    clients.withClient(
      client => client.get(key)
    )
  }


}