
import java.io.File

import com.lufi.matching.Matchers
import com.lufi.matching.matchers.{PhoneMatcher}
import org.apache.spark.{SparkConf, SparkContext}

object FindIt {

  def main(args: Array[String]): Unit = {
    val filepath = "src/main/files/sample.csv"
    val outputpath = "src/main/files/output"

    val startTime = System.currentTimeMillis()
    val conf = new SparkConf().setMaster("local[*]").setAppName("spark")
    val sc = new SparkContext(conf)
    val dataFile = sc.textFile(filepath)
    val lines = dataFile.flatMap(_.split(System.getProperty("line.separator")))

    val matchers = new Matchers
    matchers.addMatcher(PhoneMatcher.getInstance())
    val output=lines.map(line=>matchers.`match`(line))
    val path: File = new File(outputpath)
    dirDel(path)
    //是一个目录，如果需要保存为文件，需要使用hdfs
    output.repartition(1).saveAsTextFile(outputpath)
    println("successfully")
    val endTime = System.currentTimeMillis()

    println("时间:"+(endTime-startTime)+"ms")
  }




  //删除目录和文件
  def dirDel(path: File) {
    if (!path.exists())
      return
    else if (path.isFile()) {
      path.delete()
      //println(path + ":  文件被删除")
      return
    }

    val file: Array[File] = path.listFiles()
    for (d <- file) {
      dirDel(d)
    }

    path.delete()
    println(path + ":  目录被删除")

  }


}
