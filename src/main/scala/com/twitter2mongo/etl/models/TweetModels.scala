package com.twitter2mongo.etl.models

import spray.json._

object TweetModels {

  case class Location(coordinates: Array[Float], `type`: String = "Point")

  case class Tweet(userId: Long, tweetId: Long, tweet: String, createdAt: Long, location: Location)


  object Location extends DefaultJsonProtocol {
    implicit val locationFormat = jsonFormat2(Location.apply)

  }

  object Tweet extends DefaultJsonProtocol {

    implicit val categorySimpleFormat: RootJsonFormat[Tweet] =
      rootFormat(lazyFormat(jsonFormat5(Tweet.apply)))
  }

}
