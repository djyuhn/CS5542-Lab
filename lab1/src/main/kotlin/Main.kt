import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * @author djyuhn
 * 2/19/2019
 */
object Main {
    fun main(args: Array<String>) {

        // For Windows Users
        System.setProperty("hadoop.home.dir", "C:\\winutils")

        // Configuration
        val sparkConf = SparkConf().setAppName("Lab1")
                .setMaster("local[*]")
                .set("spark.executor.memory", "8g")
                .set("spark.driver.memory", "4g")

        val sc = SparkContext(sparkConf)


    }
}