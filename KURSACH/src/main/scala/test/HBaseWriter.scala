package test


import model.Traffic
import org.apache.hadoop.conf._
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.LogManager
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset



object HBaseWriter {
  private val logger = LogManager.getLogger(this.getClass)

  private val TABLE_NAME = "plain"
  private val CF_DEFAULT = "plains"
  val config: Configuration = HBaseConfiguration.create()
  config.addResource(new Path(AppProperties.hbaseSitePath))

  def createTable(): Unit = {
    logger.info(s"Create table $TABLE_NAME")
    val connection = ConnectionFactory.createConnection(config)
    val admin = connection.getAdmin
    val table: HTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME))

    if (!admin.tableExists(table.getTableName)) {
      val newColumn: HColumnDescriptor = new HColumnDescriptor(CF_DEFAULT)
      newColumn.setMaxVersions(HConstants.ALL_VERSIONS)
      table.addFamily(newColumn)
      admin.createTable(table)
    }
  }

  def insert(ds: Dataset[Traffic]): Unit = {
    val cols = ds.columns.sorted  // WARN HERE !!!!!!!!!!!!! .filter(!_.equals("id"))
    val path = "/user/admin/tmp/" + TABLE_NAME
    config.set(TableOutputFormat.OUTPUT_TABLE, TABLE_NAME)
    config.setInt(LoadIncrementalHFiles.MAX_FILES_PER_REGION_PER_FAMILY, 200)
    val crimeRDDS: RDD[Traffic] = ds.rdd.sortBy(_.origin.toString)
    val rdd: RDD[(ImmutableBytesWritable, KeyValue)] = crimeRDDS.flatMap(record => toHbaceBinary(record, cols))
    try {
      rdd.saveAsNewAPIHadoopFile(path, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2], config)
      val table = new HTable(config, TABLE_NAME)
      val bulkLoader = new LoadIncrementalHFiles(config)
      bulkLoader.doBulkLoad(new Path(path), table)
    }
    finally {
      val hdfs = FileSystem.get(config)
      val deletePaths = hdfs.globStatus(new Path(path)).map(_.getPath)
      deletePaths.foreach { path => hdfs.delete(path, true) }
    }
  }

  private def toHbaceBinary(crimeRecord: Traffic, cols: Array[String]): IndexedSeq[(ImmutableBytesWritable, KeyValue)] = {
    var hbaceBinaryCrimeRecord = new Array[(ImmutableBytesWritable, KeyValue)](cols.length)
    val rowKey = Bytes.toBytes(crimeRecord.origin.toString)
    val hkey = new ImmutableBytesWritable(rowKey)


    for (i <- 0 to cols.length - 1) yield {
                val index = crimeRecord.fieldIndex(new String(cols(i)))
                val value = crimeRecord.getVal(cols(i)).getBytes()
                val kv = new KeyValue(rowKey, CF_DEFAULT.getBytes(), cols(i).getBytes(), crimeRecord.getVal(cols(i)).getBytes())
                (hkey, kv)
              }
//    cols.map(i=> {
//      val index = crimeRecord.fieldIndex(new String(i))
//      val value = crimeRecord.getVal(i).getBytes()
//      val kv = new KeyValue(rowKey, CF_DEFAULT.getBytes(),i.getBytes(), crimeRecord.getVal(i).getBytes())
//      (hkey, kv)
//    })
//

  }
}
