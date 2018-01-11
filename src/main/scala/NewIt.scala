import java.io.{File, FileWriter}
import java.text.DecimalFormat

import scala.util.Random

object NewIt {
  def main(args: Array[String]): Unit = {
    val filePath = "src/main/files/sample.csv"
    val writer = new FileWriter(new File(filePath), false)
    for (i <- 1 to 10) {
      var phoneNum = getRandomPhoneNum()
      var bankId = getRandomBankId()
      var id = getRandomId()
      writer.write(i + "," + phoneNum + "," + bankId + "," + id)
      writer.write(System.getProperty("line.separator"))
    }

    writer.flush()
    writer.close()
    println("successfully")
  }

  def getRandomPhoneNum(): String = {
    val rand = new Random()
    var phoneNum = "1"
    for (x <- 1 to 10) {
      phoneNum += rand.nextInt(10)
    }

    return phoneNum
  }

  def getRandomBankId(): String = {
    val rand = new Random()
    val tmp = rand.nextInt(20)
    var count = 0
    if (tmp > 16) {
      count = tmp
    } else {
      count = 16
    }
    var bankId = ""
    for (x <- 1 to count) {
      bankId += rand.nextInt(10)
    }

    return bankId
  }

  def getRandomId(): String = {
    val PARITYBIT: Array[Char] = Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
    val POWER_LIST: Array[Int] = Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
    val rand = new Random()
    var power = 0
    //随机区位码
    var id = "110000"//randomNum(110000, 660000)
    //随机日期
    id += randomNum(1900, 2017)
    id += randomNum(0, 13)
    id += randomNum(0, 32)
    //随机性别
    id += randomNum(0, 999)

    val last_index = rand.nextInt(10)

    if (last_index >5) {
      for (x <- 0 to 16) {
        power += (id(x) - '0') * POWER_LIST(x)
      }

      id += PARITYBIT(power % 11)
    } else {
      id += last_index
    }

    return id

  }

  def randomNum(begin: Int, end: Int): String = {
    val rtn = begin + (Math.random * (end - begin)).toLong
    // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
    if (rtn == begin || rtn == end)
        return randomNum(begin, end)

    var count=""
    var copy_end=end
    while(copy_end!=0){
      copy_end = copy_end / 10
      count +="0"
    }
    val df = new DecimalFormat(count)

    return df.format(rtn)
  }



}
