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

  implicit class ToMapOps[A](val a: A) extends AnyVal {

    import shapeless._
    import ops.record._

    def toMap[L <: HList](implicit gen: LabelledGeneric.Aux[A, L], tmr: ToMap[L]): Map[String, Any] = {
      val m: Map[tmr.Key, tmr.Value] = tmr(gen.to(a))
      m.collect {
        case (k: Symbol, v) => k.name -> v
      }
    }
  }

}
