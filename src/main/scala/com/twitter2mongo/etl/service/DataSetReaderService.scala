package com.twitter2mongo.etl.service

import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicInteger

import akka.stream.scaladsl.Source
import better.files._
import com.twitter2mongo.etl.common.Helpers._
import com.twitter2mongo.etl.config.AppConfig._
import com.twitter2mongo.etl.models.TweetModels._
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

trait DataSetReaderService {
  this: StrictLogging =>
  lazy val defaultCharset = Charset.forName("ISO-8859-1")

  def createCombinedDatasetSource() = {
    val fileNames = List(testTweetsFileName, trainingTweetsFileName).map(dataFolder + _)

    val userLocations: Map[Long, String] = generateUserLocationDictionary()

    logger.info(s"Parsing ${fileNames.size} data set files...")
    val counter = new AtomicInteger

    Source(fileNames)
      .map(fileName => File(fileName))
      .flatMapConcat(reader => Source.fromIterator(() => reader.lineIterator(defaultCharset)))
      .map(_.split("\t"))
      .collect {
        case Array(userIdText, tweetIdText, tweet, createdAt) =>
          if (counter.incrementAndGet % 1000 == 0) logger.info(s"Parsed ${counter.get} tweet lines")

          for {
            ts <- getTimestamp(createdAt)
            userId <- Try(userIdText.toLong).toOption
            tweetId <- Try(tweetIdText.toLong).toOption
            userLoc <- userLocations.get(userId)
              .map(_
                .replaceAll("UT:", "")
                .trim
                .split(",")
                .flatMap(n => Try(n.toFloat).toOption)
                .reverse)
              .filter(_.length == 2)
              .map(coord => Location(coord))
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

    val dictionary = fileNames.map(fileName => File(fileName))
      .map(_.lineIterator(defaultCharset).map(_.split("\t")))
      .map(_.collect {
        case Array(userId, locationString) =>
          if (counter.incrementAndGet % 1000 == 0) logger.info(s"Parsed ${counter.get} user lines")
          Try(userId.toLong).toOption.map(id => (id, locationString))
      }.flatten.toMap)
      .reduce(_ ++ _)

    logger.info(s"Got ${dictionary.size} user locations.")
    dictionary
  }

}
