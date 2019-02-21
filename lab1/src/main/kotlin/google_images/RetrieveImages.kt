package google_images

import image_retrieve.ImageRetrieval
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/20/2019
 */

fun main(args:Array<String>) {

    try {
        val bufferedReader = File("data/categorized/google_images.txt").bufferedReader()
        bufferedReader.forEachLine { line ->
            val url = line.split("\t")[2]
            ImageRetrieval.getURLImage(url, "data/GCC_set")
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }


}