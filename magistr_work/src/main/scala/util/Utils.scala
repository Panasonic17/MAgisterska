package util

import entity.Historical

object Utils {

  def getSlicedValues[T](data: Seq[T], count: Int): Seq[T] = {
    val step = data.size / count
    for (i <- Range(0, data.size, step)) yield {
      data(i)
    }
  }

  def isFullHistoracal(historical: Historical): Boolean = {

    if (historical.traectory.length < settings.Constants.TRAECTORY_SLICING_COUNT) return false

    if (historical.iataDest == null) return false
    if (historical.iataOrigin == null) return false
    if (historical.timeData.realArrival <10) return false
    if (historical.timeData.realDeparture <10) return false
    if (historical.timeData.scheduledDeparture <10) return false
    if (historical.timeData.scheduledArrival <10) return false
    return true

  }

}

