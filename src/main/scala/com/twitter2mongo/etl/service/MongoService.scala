package com.twitter2mongo.etl.service

import com.twitter2mongo.etl.models.TweetModels.Tweet
import com.typesafe.scalalogging.StrictLogging
import com.mongodb.casbah.Imports._
import salat._
import salat.global._


trait MongoService {
  this: StrictLogging =>

  val mongoClient: MongoClient
  lazy val database = mongoClient.getDB("twitter")

  def insertTweet(tweet: Tweet) = {
    val tweetCollection = database.getCollection("tweets")
    val dbo = grater[Tweet].asDBObject(tweet)
    tweetCollection.insert(dbo)
  }
}
