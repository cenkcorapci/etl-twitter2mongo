package com.twitter2mongo.etl.config

import com.typesafe.config.ConfigFactory

object AppConfig {

  val config = ConfigFactory.load()

  //App
  val dataFolder = config.getString("app.dataFolder")
  val testUsersFileName = config.getString("app.testUsersFileName")
  val trainingUsersFileName = config.getString("app.trainingUsersFileName")
  val testTweetsFileName = config.getString("app.testTweetsFileName")
  val trainingTweetsFileName = config.getString("app.trainingTweetsFileName")

  //MongoDB
  val mongoURL = config.getString("mongo.connectionUrl")

  //Stream
  val defaultParallelism = config.getInt("stream.defaultParallelism")
}
