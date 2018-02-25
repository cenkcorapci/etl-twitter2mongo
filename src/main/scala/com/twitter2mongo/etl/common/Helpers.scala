package com.twitter2mongo.etl.common

import java.sql.Timestamp
import java.text.SimpleDateFormat

import scala.util.{Failure, Success, Try}

object Helpers {
  /**
    * example format 2010-03-15 11:36:46
    *
    * @param s
    * @return
    */
  def getTimestamp(s: String): Option[Long] = s match {
    case "" => None
    case _ => {
      val format = new SimpleDateFormat("MM-dd-yyyy' 'HH:mm:ss")
      Try(new Timestamp(format.parse(s).getTime)) match {
        case Success(t) => Some(t.getTime)
        case Failure(_) => None
      }
    }
  }
}
