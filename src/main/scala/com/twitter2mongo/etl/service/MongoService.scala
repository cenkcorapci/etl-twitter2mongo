package com.twitter2mongo.etl.service

import com.mongodb.casbah.Imports._
import com.twitter2mongo.etl.common.Helpers._
import com.twitter2mongo.etl.models.TweetModels.Tweet
import com.typesafe.scalalogging.StrictLogging

trait MongoService {
  this: StrictLogging =>

  val mongoClient: MongoClient
  lazy val database = mongoClient.getDB("twitter")

  def insertTweet(tweet: Tweet) = {
    val data = tweet.toMap
    val tweetCollection = database.getCollection("tweets")

    val builder = MongoDBObject.newBuilder
    data.foreach(builder += _)
    tweetCollection.insert(builder.result)
  }
}
