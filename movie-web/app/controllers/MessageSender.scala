package producer

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

object MessageSender {
  /**
   * sendRatingToKafka(ratingform)
   *
   * ratingform should be a tuple of 4 strings: (userId,movieId,rating,timestamp)
   */
  def sendRatingToKafka(ratingform: (String, String, String, String)): Unit = {
    // get Kafka properties object
    val properties = MyProducer.buildKafkaProperties()
    // create producer
    val producer = new KafkaProducer[String, String](properties)
    // send message
    val rating: String = ratingform._1 + "," + ratingform._2 + "," + ratingform._3 + "," + ratingform._4
    val ratingRecord = new ProducerRecord[String, String]("ratings", "r", rating) // data
//    producer.send(ratingRecord, (metadata: RecordMetadata, exception: Exception) => if (exception != null) {
//      println("can not send user message to topic 'rating'!")
//    })
    producer.send(ratingRecord)
    producer.close()
  }
}
