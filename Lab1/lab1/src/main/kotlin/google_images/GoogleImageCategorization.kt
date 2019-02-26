package google_images

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import scala.Tuple2
import java.io.BufferedWriter
import java.io.FileWriter

/**
 * @author djyuhn
 * 2/20/2019
 */
fun main(args: Array<String>) {
    val terrainTypesFile = "data/categories/categories.txt"
    val gccFile = "data/GCC_set/Train_GCC-training.tsv"
    val categorizedFolder = "data/categorized/"

    // For Windows Users
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Configuration
    val sparkConf = SparkConf().setAppName("Lab1")
            .setMaster("local[*]")

    val sc = JavaSparkContext(sparkConf)

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

    val googleFile = sc.textFile(gccFile).map{line ->
        val categorized = StringBuilder()

        val splitLine = line.split("\t")
        val tuple = categoryBroadcast.value

        tuple.forEach{(key, value) ->
            val key_regex = Regex("(?:^|\\W)$key(?:\$|\\W)")
            if (splitLine[0].contains(key_regex))
                categorized.append(key).append("\t").append(splitLine[0]).append("\t").append(splitLine[1]).append("\n")
            else {
                for (word in value) {
                    val word_regex = Regex("(?:^|\\W)$word(?:\$|\\W)")
                    if (splitLine[0].matches(word_regex)) {
                        categorized.append(key).append("\t").append(splitLine[0]).append("\t").append(splitLine[1]).append("\n")
                        break
                    }
                }
            }
        }

        categorized.toString()
    }.filter{ it != ""}

    val categorizedGoogleImages = BufferedWriter(FileWriter(categorizedFolder + "google_images.txt"))

    googleFile.collect().forEach{ file ->
        val splitLines = file.split("\n")
        splitLines.forEach { line ->
            if (!line.equals(""))
                categorizedGoogleImages.append(line).append("\n")
        }
    }
}
