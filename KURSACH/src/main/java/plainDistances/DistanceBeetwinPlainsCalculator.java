package plainDistances;

import model.PlainPosition;
import org.apache.spark.streaming.dstream.DStream;
import scala.Int;
import scala.Tuple1;
import scala.Tuple12;
import scala.Tuple2;
import spark.streaming.distance.Utils;

public class DistanceBeetwinPlainsCalculator {

    Utils utils;

    public void calcultaeDistances(DStream<Tuple2<Int,PlainPosition>> dstream) {

//        dstream.map(data-> new Tuple2<Integer,PlainPosition>(2,data))
//        dstream.map(PlainPosition -> new Tuple2<Integer,PlainPosition>(PlainPosition.getCoorditationsKey(),PlainPosition));
    }
}
