package com.twitter2mongo.etl.service

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import akka.stream.scaladsl.Source
import com.github.tototoshi.csv._
import com.twitter2mongo.etl.common.Helpers._
import com.twitter2mongo.etl.config.AppConfig._
import com.twitter2mongo.etl.models.TweetModels._
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

trait DataSetReaderService {
  this: StrictLogging =>

  private implicit object MyFormat extends DefaultCSVFormat {
    override val delimiter = '\t'
  }

  def createCombinedDatasetSource() = {
    val fileNames = List(testTweetsFileName, trainingTweetsFileName).map(dataFolder + _)

    val userLocations: Map[Long, String] = generateUserLocationDictionary()

    logger.info(s"Parsing ${fileNames.size} data set files...")
    val counter = new AtomicInteger

    Source(fileNames)
      .map(fileName => CSVReader.open(new File(fileName)))
      .flatMapConcat(reader => Source.fromIterator(() => reader.iterator))
      .map { line =>
        logger.info(line.mkString(" ||| "))
        line
      }
      .collect {
        case Seq(userIdText, tweetIdText, tweet, createdAt) =>
          if (counter.incrementAndGet % 1000 == 0) logger.info(s"Parsed ${counter.get} tweet lines")

          for {
            ts <- getTimestamp(createdAt)
            userId <- Try(userIdText.toLong).toOption
            tweetId <- Try(tweetIdText.toLong).toOption
            userLoc <- userLocations.get(userId)
          } yield Tweet(userId, tweetId, tweet, ts, userLoc)
      }
      .collect {
        case Some(tweet) => tweet
      }
  }

  def generateUserLocationDictionary() = {

    val fileNames = List(testUsersFileName, trainingUsersFileName).map(dataFolder + _)
    logger.info(s"Parsing ${fileNames.size} user data files...")

    val counter = new AtomicInteger

    val dictionary = fileNames.map(fileName => CSVReader.open(new File(fileName)))
      .map(_.all)
      .map(_.collect {
        case List(userId, locationString) =>
          if (counter.incrementAndGet % 1000 == 0) logger.info(s"Parsed ${counter.get} user lines")
          Try(userId.toLong).toOption.map(id => (id, locationString))
      }.flatten.toMap)
      .reduce(_ ++ _)

    logger.info(s"Got ${dictionary.size} user locations.")
    dictionary
  }

}
