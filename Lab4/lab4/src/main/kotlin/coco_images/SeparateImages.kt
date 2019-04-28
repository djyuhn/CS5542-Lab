package coco_images

import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/21/2019
 */

fun main(args:Array<String>) {

    val imageDirectory = "C:\\School\\Images\\MSCOCO\\val2017\\"
    val categorizedImageDirectory = "C:\\School\\Images\\categorized\\combined_val\\"
    val readFile = "data/categorized/coco_images_val.txt"
    try {
        val bufferedReader = File(readFile).bufferedReader()
        bufferedReader.forEachLine { line ->
            val splitLine = line.split("\t")
            if (splitLine.size == 3) {
                val filename = splitLine[2]
                val file = File("$imageDirectory$filename.jpg")
                val copyFile = File("$categorizedImageDirectory/$filename.jpg")
                file.copyTo(copyFile, overwrite = true)
            }
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }
}
