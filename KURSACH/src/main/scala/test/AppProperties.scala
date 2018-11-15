package test

import java.io.InputStream
import java.util.Properties

object AppProperties {

  private def loadProperties: Properties = {
    val _prop = new java.util.Properties()
    val stream: InputStream = this.getClass.getClassLoader.getResourceAsStream("app.properties")
    _prop.load(stream)
    _prop
  }
  private val prop = loadProperties

  lazy val hiveMetastoreUris: String = prop.getProperty("hive_metastore_uris")

  def fileHdfsPath(path: Option[String]): String = path.map(_ + "/").getOrElse(prop.getProperty("file_hdfs_path"))

  lazy val hbaseSitePath: String = this.getClass.getClassLoader.getResource("hbase-site.xml").getPath

  lazy val locationForHiveExternalTable: String = prop.getProperty("location_for_hive_external_table")
}
