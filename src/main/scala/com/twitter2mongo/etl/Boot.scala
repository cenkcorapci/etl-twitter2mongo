package com.twitter2mongo.etl

import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Sink
import com.twitter2mongo.etl.service.{DataSetReaderService, MongoService}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Boot extends App
  with DataSetReaderService
  with MongoService
  with AppContext
  with StrictLogging {

  override val mongoClient = defaultMongoClient

  val dataSetSource = createCombinedDatasetSource()
  dataSetSource
    .buffer(256, OverflowStrategy.backpressure)
    .map(insertTweet)
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
