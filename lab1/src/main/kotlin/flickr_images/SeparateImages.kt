package flickr_images

import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/21/2019
 */

fun main(args:Array<String>) {

    val flickrImageDirectory = "data/flicker/Flicker8k_Dataset/"
    val flickrCategorizedImageDirectory = "data/categorized/flickr_categorized_images/"
    try {
        val bufferedReader = File("data/categorized/flickr_images.txt").bufferedReader()
        bufferedReader.forEachLine { line ->
            val splitLine = line.split("\t")
            if (splitLine.size == 3) {
                val filename = splitLine[1].substring(0, splitLine[1].length - 2)
                val category = splitLine[0]
                val file = File(flickrImageDirectory + filename)
                val copyFile = File(flickrCategorizedImageDirectory + category + "/" + filename)
                file.copyTo(copyFile, overwrite = true)
            }
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }



}