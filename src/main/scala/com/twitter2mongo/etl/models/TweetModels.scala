package com.twitter2mongo.etl.models

object TweetModels {

  case class Tweet(userId: Long, tweetId: Long, tweet: String, createdAt: Long, location: String)

}
