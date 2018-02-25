package com.twitter2mongo.etl.config

import akka.stream.Supervision
import com.typesafe.scalalogging.StrictLogging

object SupervisionSettings extends StrictLogging {
  val resumeOnError: Supervision.Decider = {
    case e =>
      println(e)
      logger.error("Unhandled exception in stream", e)
      Supervision.resume
  }
}

