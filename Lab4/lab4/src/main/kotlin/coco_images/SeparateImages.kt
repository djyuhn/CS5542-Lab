package coco_images

import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/21/2019
 */

fun main(args:Array<String>) {

    val imageDirectory = "D:\\School\\BigData-Lab4\\Images\\MSCOCO\\val2017\\"
    val categorizedImageDirectory = "D:\\School\\BigData-Lab4\\Images\\categorized\\coco_val\\"
    val readFile = "data/categorized/coco_val_ids.txt"
    try {
        val bufferedReader = File(readFile).bufferedReader().readLines()
        bufferedReader.forEach { line ->
            val filename = line.replace("\\s".toRegex(), "")
            val file = File("$imageDirectory/$filename.jpg")
            val copyFile = File("$categorizedImageDirectory/$filename.jpg")
            file.copyTo(copyFile, overwrite = true)
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }
}
