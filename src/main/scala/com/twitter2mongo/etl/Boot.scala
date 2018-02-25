package com.twitter2mongo.etl

import com.twitter2mongo.etl.service.DataSetReaderService
import com.typesafe.scalalogging.StrictLogging


object Boot extends App
  with DataSetReaderService
  with AppContext
  with StrictLogging {

  val dataSetSource = createCombinedDatasetSource()
  dataSetSource.map(_.tweet).map(t => logger.info(t))
}
