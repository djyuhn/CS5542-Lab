package flickr_images

import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/21/2019
 */

fun main(args:Array<String>) {

    val imageDirectory = "C:\\School\\Images\\Flickr8k\\Flicker8k_Dataset\\"
    val categorizedImageDirectory = "C:\\School\\Images\\categorized\\combined\\"
    val readFile = "data/categorized/flickr_images_unchanged_captions.txt"
    try {
        val bufferedReader = File(readFile).bufferedReader()
        bufferedReader.forEachLine { line ->
            val splitLine = line.split("\t")
            if (splitLine.size == 3) {
                val filename = splitLine[2].substring(0, splitLine[2].length - 2)
                val file = File(imageDirectory + filename)
                val copyFile = File("$categorizedImageDirectory/$filename.jpg")
                file.copyTo(copyFile, overwrite = true)
            }
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }



}