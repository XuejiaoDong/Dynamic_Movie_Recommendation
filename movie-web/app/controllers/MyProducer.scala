package producer

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import java.util.Properties

import edu.neu.RSConfig

import scala.annotation.tailrec

object MyProducer {

  def buildKafkaProperties(): Properties = {
    // set env properties
//    val configs = ConfigFactory.load()
//    val envProps = configs.getConfig("dev")
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
    // load and send file
    import scala.io.Source
    val source = Source.fromFile("src/main/resources/ratings.csv")
    val iter: Iterator[String] = source.getLines()

    val MAX_SEND_COUNT = 2000
//    val MAX_SEND_COUNT = ConfigFactory
//      .load()
//      .getConfig("dev")
//      .getString("consumerSpeed")  // 2000

    // timerSend: send 2000 messages and sleep for 1 second
    @tailrec
    def timerSend(count: Int): Any = {
      if (iter.hasNext) {
        val ratingLog = iter.next()
        val ratingRecord = new ProducerRecord[String, String]("ratings", "r", ratingLog) // data
        producer.send(ratingRecord, new Callback {
          override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
            if(exception != null){
              println("can not send file message to topic 'rating'!")
            }
        })
      } else {
        return
      }
      if(count == MAX_SEND_COUNT) {
        println(count)
        Thread.sleep(1000L)
        timerSend(0)
      } else timerSend(count + 1)
    }
    timerSend(0)
    // housekeeping
    source.close()
    producer.close()
  }

}
