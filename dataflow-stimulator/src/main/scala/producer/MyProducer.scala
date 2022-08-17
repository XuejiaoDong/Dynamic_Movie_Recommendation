package producer

import com.typesafe.config.{Config, ConfigFactory}
import edu.neu.mysql.MySQLUtils
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import java.util.Properties

import edu.neu.RSConfig

import scala.annotation.tailrec

object MyProducer extends App {

  sendMessage()

  def buildKafkaProperties(): Properties = {
    // set env properties
    // val configs = ConfigFactory.load()
    // val envProps = configs.getConfig("dev")
    // build kafka properties
    val properties = new Properties()
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, RSConfig.kafka_server)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties
  }

  def sendMessage(): Unit = {
    // get Kafka properties object
    val properties = buildKafkaProperties()
    // create producer
    val producer = new KafkaProducer[String, String](properties)
    // load file, make it iterable
    import scala.io.Source
    val source = Source.fromFile("src/main/resources/ratings.csv")
    val iter: Iterator[String] = source.getLines()
    // set max send count
    val MAX_SEND_COUNT = 2000
    //    val MAX_SEND_COUNT = ConfigFactory
    //      .load()
    //      .getConfig("dev")
    //      .getString("consumerSpeed")  // 2000

    // timerSend: send 2000 messages and sleep for 1 second
    @tailrec
    def timerSend(count: Int): Any = {

      /**
       * Recursion end condition:
       * cannot read the next line of file
       */
      if (iter.hasNext) {

        /**
         * file has next line:
         * 1. read the line named ratingLog.
         * 2. build a ratingRecord with topic, key, and ratingLog.
         * p.s. ratingRecord is what we will send to Kafka
         */
        val ratingLog = iter.next()
        val ratingRecord = new ProducerRecord[String, String]("ratings", "r", ratingLog)

        // send ratingRecord with producer
        producer.send(ratingRecord, new Callback {
          override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
            if (exception != null) {
              println("can not send file message to topic 'rating'!")
            }
        })
      } else {
        /**
         * file doesn't have next line:
         * return to the previous recursion
         */
        return
      }

      /**
       * Recursion process:
       * 1. if count is up to MAX_SEND_COUNT:
       * send count to MySQL, sleep for 1 second,
       * and send count = 0 to next recursion.
       *
       * 2. if count is less than MAX_SEND_COUNT:
       * send count + 1 to next recursion
       */
      if (count == MAX_SEND_COUNT) {
        println("------------------------->>>>>>>>>>>>>>>>>>>>>")
        println("send %s msg to kafka".format(count))
        // send 2000 to MySQL table named input
        val sql = "INSERT INTO input(amount) VALUES(2000)"
        val res = MySQLUtils.insertOrUpdate(sql)
        println("res=" + res)

        Thread.sleep(1000L) // todo:
        timerSend(0)
      } else timerSend(count + 1)
    }

    timerSend(0)
    // housekeeping
    source.close()
    producer.close()
  }

}
