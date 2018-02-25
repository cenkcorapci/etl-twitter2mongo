package com.twitter2mongo.etl.service

import com.typesafe.scalalogging.StrictLogging
import org.mongodb.scala._

trait MongoService {

  this: StrictLogging =>
  val mongoClient: MongoClient
}
