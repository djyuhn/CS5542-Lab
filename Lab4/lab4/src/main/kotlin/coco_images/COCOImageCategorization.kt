package coco_images

import openie.CoreNLP
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import scala.Tuple2
import java.io.BufferedWriter
import java.io.FileWriter

/**
 * @author djyuhn
 * 2/20/2019
 */
fun main(args: Array<String>) {
    val terrainTypesFile = "data/categories/categories.txt"
    val cocoFilePath = "D:\\School\\BigData-Lab4\\Images\\MSCOCO\\texts\\captions_val2017.json"
    val categorizedFolder = "data/categorized/"

    // For Windows Users
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Configuration
    val sparkConf = SparkConf().setAppName("Lab4")
            .setMaster("local[*]")
            .set("spark.executor.memory", "4g")
            .set("spark.driver.memory", "10g")

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

    val jsonContents = sqlContext.read().json(cocoFilePath)

    //To categorize images and maintain original captions
    val imageContents: Array<out Row> = jsonContents.select("annotations").collect() as Array<out Row>
    val imageCaptions = imageContents.map{row -> row.getSeq<Row>(0) }
            .map{image ->
                val categorized = StringBuilder()
                val tuple = categoryBroadcast.value

                image.foreach{ row: Row ->
                    val imageID = row.getLong(2)
                    val caption = row.getString(0)

                    tuple.forEach{(key, value) ->
                        val key_regex = Regex("(?:^|\\W)$key(?:\$|\\W)")
                        if (caption.contains(key_regex)) {
                            categorized.append(imageID).append("\n")
                        }
                        else {
                            for (word in value) {
                                val word_regex = Regex("(?:^|\\W)$word(?:\$|\\W)")
                                if (caption.contains(word_regex)) {
                                    categorized.append(imageID).append("\n")
                                    break
                                }
                            }
                        }
                    }
                }
                categorized.toString()
            }.distinct()


    val categorizedCOCOImages = BufferedWriter(FileWriter(categorizedFolder + "coco_val_ids.txt"))

    val image_ids = hashSetOf<String>()
    imageCaptions.forEach{ image ->
        val splitLines = image.split("\n")
        splitLines.forEach { line ->
            if (!line.equals(""))
                image_ids.add(line);
        }
    }

    image_ids.forEach{ id ->
        categorizedCOCOImages.append(id).append("\n")
    }

    categorizedCOCOImages.close()
}
