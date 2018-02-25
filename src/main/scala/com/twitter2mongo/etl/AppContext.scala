package com.twitter2mongo.etl

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.twitter2mongo.etl.config.SupervisionSettings.resumeOnError
import com.typesafe.scalalogging.StrictLogging

trait AppContext {
  this: StrictLogging =>
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(resumeOnError))

  protected def releaseResources(withStatusCode: Int = 0) = {
    logger.info("Releasing resources.")
    system.terminate()
    System.exit(withStatusCode)
  }
}
