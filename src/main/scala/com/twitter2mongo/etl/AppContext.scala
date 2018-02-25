package com.twitter2mongo.etl

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.mongodb.casbah.MongoClient
import com.twitter2mongo.etl.config.SupervisionSettings.resumeOnError
import com.typesafe.scalalogging.StrictLogging
import com.twitter2mongo.etl.config.AppConfig._

trait AppContext {
  this: StrictLogging =>
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(resumeOnError))

  val defaultMongoClient = if (mongoURL.isEmpty) MongoClient() else MongoClient(mongoURL)

  protected def releaseResources(withStatusCode: Int = 0) = {
    logger.info("Releasing resources.")
    system.terminate()
    System.exit(withStatusCode)
  }
}
