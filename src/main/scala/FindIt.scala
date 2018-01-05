<<<<<<< HEAD
//import java.io.File
//
//import com.lufi.regex.{BankInfoUtil, IdInfoUtil, PhoneInfoUtil}
//import org.apache.spark.{SparkConf, SparkContext}
//
//object FindIt {
//
//  def main(args: Array[String]): Unit = {
//    val filepath = "src/main/files/sample.csv"
//    val outputpath = "src/main/files/output"
//
//    val startTime = System.currentTimeMillis()
//    val conf = new SparkConf().setMaster("local[*]").setAppName("spark")
//    val sc = new SparkContext(conf)
//    val dataFile = sc.textFile(filepath)
//    val lines = dataFile.flatMap(_.split(System.getProperty("line.separator")))
//    val output=lines.map(line=>matchx(line))
//    val path: File = new File(outputpath)
//    dirDel(path)
//    //是一个目录，如果需要保存为文件，需要使用hdfs
//    output.repartition(1).saveAsTextFile(outputpath)
//    println("successfully")
//    val endTime = System.currentTimeMillis()
//
//    println("时间:"+(endTime-startTime)+"ms")
//  }
//
//
//  def matchx(x:String):String = {
//    val splitx = x.split(",")
//    val count = splitx.size
//    var ret = ""
//    for (i <- 0 to count - 1) {
//      val element = splitx(i)
//      if (PhoneInfoUtil.isTelNumber(element)) {
//        ret+="电话号码" + " "
//      } else if (IdInfoUtil.isValid(element)) {
//        ret+="身份证号" + " "
//      } else if (BankInfoUtil.isValid(element)) {
//        ret+="银行卡号" + " "
//      } else {
//        ret+=splitx(i) + " "
//      }
//    }
//
//    return ret
//  }
//
//  //删除目录和文件
//  def dirDel(path: File) {
//    if (!path.exists())
//      return
//    else if (path.isFile()) {
//      path.delete()
//      //println(path + ":  文件被删除")
//      return
//    }
//
//    val file: Array[File] = path.listFiles()
//    for (d <- file) {
//      dirDel(d)
//    }
//
//    path.delete()
//    println(path + ":  目录被删除")
//
//  }
//
//
//}
=======
import java.io.File

import com.lufi.regex.{BankInfoUtil, IdInfoUtil, PhoneInfoUtil}
import org.apache.spark.{SparkConf, SparkContext}

object FindIt {

  def main(args: Array[String]): Unit = {
    val filepath = "src/main/files/sample.csv"
    val outputpath = "src/main/files/output"

    val conf = new SparkConf().setMaster("local[*]").setAppName("spark")
    val sc = new SparkContext(conf)

    val dataFile = sc.textFile(filepath)
    val lines = dataFile.flatMap(_.split(System.getProperty("line.separator")))

    val output=lines.map(line=>matchx(line))
    //output.collect().foreach(x => println(x))
    //是一个目录，如果需要保存为文件，需要使用hdfs
    val path: File = new File(outputpath)
    dirDel(path)
    output.repartition(1).saveAsTextFile(outputpath)
    println("successfully")
  }


  def matchx(x:String):String = {
    val splitx = x.split(",")
    val count = splitx.size
    var ret = ""
    for (i <- 0 to count - 1) {
      val element = splitx(i)
      if (PhoneInfoUtil.isTelNumber(element)) {
        //ret+=PhoneInfoUtil.ofTelNumber(splitx(i))+" "
        ret+="电话号码" + " "
      } else if (IdInfoUtil.isValid(element)) {
        ret+="身份证号" + " "
      } else if (BankInfoUtil.isValid(element)) {
        ret+="银行卡号" + " "
      } else {
        ret+=splitx(i) + " "
      }
    }

    return ret
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
>>>>>>> 735f7d5c563ba1934676e8e4be87cc65cc533303
