package com.twitter2mongo.etl.service

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import com.twitter2mongo.etl.models.TweetModels.Tweet
import com.typesafe.scalalogging.StrictLogging
import spray.json._

trait MongoService {
  this: StrictLogging =>

  val mongoClient: MongoClient
  lazy val database = mongoClient.getDB("twitter")
  lazy val tweetCollection = database.getCollection("tweets")

  def insertTweet(tweet: Tweet) = {
    val jsonString = tweet.toJson.compactPrint
    val dbObject: DBObject = JSON.parse(jsonString).asInstanceOf[DBObject]
    tweetCollection.insert(dbObject)
  }
}
