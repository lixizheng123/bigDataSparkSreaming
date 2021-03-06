package com.fengli.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * Spark Streaming 处理Socket数据
 */
object NetworkWordCount {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")

    // 创建StreamingContext需要 SparkConf 和 batch interval
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.socketTextStream("192.168.10.134", 6789)

    val result: DStream[(String, Int)] = lines
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
