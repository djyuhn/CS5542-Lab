package coco_images

import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/21/2019
 */

fun main(args:Array<String>) {

    val imageDirectory = "C:\\Users\\djyuhn\\Downloads\\COCO_ImageSet\\"
    val categorizedImageDirectory = "data/categorized/coco_categorized_images/"
    val readFile = "data/categorized/coco_images.txt"
    try {
        val bufferedReader = File(readFile).bufferedReader()
        bufferedReader.forEachLine { line ->
            val splitLine = line.split("\t")
            if (splitLine.size == 3) {
                val filename = splitLine[2]
                val category = splitLine[0]
                val file = File("$imageDirectory$filename.jpg")
                val copyFile = File("$categorizedImageDirectory$category/$filename.jpg")
                file.copyTo(copyFile, overwrite = true)
            }
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }
}
