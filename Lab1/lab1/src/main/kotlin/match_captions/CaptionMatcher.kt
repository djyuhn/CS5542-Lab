package match_captions

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import scala.Tuple2
import java.io.BufferedWriter
import java.io.FileWriter

/**
 * @author djyuhn
 * 3/11/2019
 */

fun main(args: Array<String>) {
    val generatedCaptionFile = "data/generated_captions/generated_captions.txt"
    val originalCaptionFile = "data/categorized/coco_images_original.txt"
    val mergedCaptionsFile = "data/merged_captions.txt"

    // For Windows Users
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Configuration
    val sparkConf = SparkConf().setAppName("Lab2")
            .setMaster("local[*]")
            .set("spark.executor.memory", "4g")
            .set("spark.driver.memory", "10g")

    val sc = JavaSparkContext(sparkConf)
    val sqlContext = SQLContext(sc)

    val originalCaptions = sc.textFile(originalCaptionFile)

    val originalCaptionsTuple = originalCaptions.mapToPair {line ->
        val splitLine = line.split("\t")
        var tuple: Tuple2<String, String> = Tuple2("","")

        if (splitLine.size == 3) {
            tuple = Tuple2(splitLine[2], splitLine[1])
        }

        tuple

    }.filter{it != Tuple2("","")}

    val originalCaptionBroadcast = sc.broadcast(originalCaptionsTuple.collectAsMap())

    val generatedCaptions = sc.textFile(generatedCaptionFile).map{line ->

        val splitLines = line.split("\t")
        val id = splitLines[0].removeSuffix(".jpg")
        val generatedCaptionsSeparate = splitLines.drop(1).joinToString("\t")
        val tuple = originalCaptionBroadcast.value
        val originalCaption = tuple[id]

        val stringBuilder = StringBuilder()

        stringBuilder.append(id)
                .append("\t")
                .append(originalCaption)
                .append("\t")
                .append(generatedCaptionsSeparate)
                .append("\n")

        stringBuilder.toString()
    }.filter{ it != ""}

    val mergedCaptions = BufferedWriter(FileWriter(mergedCaptionsFile))

    generatedCaptions.collect().forEach { captions ->
        val splitLines = captions.split("\n")
        splitLines.forEach { line ->
            if (!line.equals(""))
                mergedCaptions.append(line).append("\n")
        }
    }

    mergedCaptions.close()
}
