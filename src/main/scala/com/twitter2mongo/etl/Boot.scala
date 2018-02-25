package com.twitter2mongo.etl

import akka.stream.scaladsl.Sink
import com.twitter2mongo.etl.service.DataSetReaderService
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Boot extends App
  with DataSetReaderService
  with AppContext
  with StrictLogging {

  val dataSetSource = createCombinedDatasetSource()
  dataSetSource.map(_.tweet)
    .map(t => logger.info(t))
    .map(_ => 1)
    .runWith(Sink.fold(0)(_ + _))
    .onComplete {
      case Success(total) =>
        println(s"Parsed $total tweets.")
        releaseResources()
      case Failure(cause) =>
        logger.error("Can't parse tweets.", cause)
        releaseResources(1)
    }
}
