package test

import scala.collection.immutable

object arrSlice {
  def main(args: Array[String]): Unit = {
    val r: immutable.Seq[Int] = Range(1, 20 + 1)
    val number = 10
    val step = r.size / number

    println(r)
    println(number)
    println(step)
    println()
    println(getSlicedValues(r, number))
  }

  def getSlicedValues[T](data: Seq[T], count: Int): Seq[T] = {
    val step = data.size / count
    for (i <- Range(0, data.size, step)) yield {
      data(i)
    }
  }

}
