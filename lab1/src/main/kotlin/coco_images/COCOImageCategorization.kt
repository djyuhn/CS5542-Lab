package coco_images

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SparkSession
import scala.Tuple2
import scala.util.parsing.json.`package`
import java.io.BufferedWriter
import java.io.FileWriter

/**
 * @author djyuhn
 * 2/20/2019
 */
fun main(args: Array<String>) {
    val terrainTypesFile = "data/categories/terrain_types.txt"
    val cocoFilePath = "data/COCO/captions_train2017_2.json"
    val categorizedFolder = "data/categorized/"

    // For Windows Users
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Configuration
    val sparkConf = SparkConf().setAppName("Lab1")
            .setMaster("local[*]")
            .set("spark.executor.memory", "4g")
            .set("spark.driver.memory", "10gb")

    val sc = JavaSparkContext(sparkConf)
    val sqlContext = SQLContext(sc)

    val categoryFile = sc.textFile(terrainTypesFile)

    val categoryTuple = categoryFile.mapToPair {line ->
        val splitLine = line.toLowerCase().split("\t")
        var synonyms: List<String> = emptyList()
        if (splitLine.size > 1) {
            synonyms = splitLine.drop(1)
        }

        Tuple2(splitLine[0], synonyms)

    }

    val categoryBroadcast = sc.broadcast(categoryTuple.collectAsMap())

    val cocoFile = sc.textFile(cocoFilePath)
    val jsonContents = sqlContext.read().json(cocoFilePath).toJavaRDD()

    val imageCaptions = jsonContents.map{ line ->
        val categorized = StringBuilder()
        val caption = line.get(0).toString()
        val imageID = line.get(2).toString()

        val tuple = categoryBroadcast.value

        tuple.forEach{(key, value) ->
            val key_regex = Regex("(?:^|\\W)$key(?:\$|\\W)")
            if (caption.contains(key_regex))
                categorized.append(key).append("\t").append(caption).append("\t").append(imageID).append("\n")
            else {
                for (word in value) {
                    val word_regex = Regex("(?:^|\\W)$word(?:\$|\\W)")
                    if (caption.matches(word_regex)) {
                        categorized.append(key).append("\t").append(caption).append("\t").append(imageID).append("\n")
                        break
                    }
                }
            }
        }

        categorized.toString()
    }

    val categorizedCOCOImages = BufferedWriter(FileWriter(categorizedFolder + "coco_images.txt"))

    imageCaptions.collect().forEach{ file ->
        val splitLines = file.split("\n")
        splitLines.forEach { line ->
            if (!line.equals(""))
                categorizedCOCOImages.append(line).append("\n")
        }
    }
}
