package flickr_images

import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/21/2019
 */

fun main(args:Array<String>) {

    val imageDirectory = "D:\\School\\BigData-Lab4\\Images\\Flickr8k\\Flicker8k_Dataset"
    val categorizedImageDirectory = "D:\\School\\BigData-Lab4\\Images\\categorized\\flickr\\"
    val readFile = "data/categorized/flickr_images_ids_keras.txt"
    try {
        var count = 0;
        val bufferedReader = File(readFile).bufferedReader().readLines()
        bufferedReader.forEach { line ->
            val filename = line.replace("\\s".toRegex(), "")
            val file = File("$imageDirectory/$filename")
            val copyFile = File("$categorizedImageDirectory/$filename")
            file.copyTo(copyFile, overwrite = true)
            if (count % 100 == 0) {
                println(count)
            }
            count++
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }



}