package google_images

import image_retrieve.ImageRetrieval
import java.io.File
import java.io.IOException

/**
 * @author djyuhn
 * 2/20/2019
 */

fun main(args:Array<String>) {

    try {
        val bufferedReader = File("data/categorized/google_images_urls.txt").bufferedReader()
        bufferedReader.forEachLine { line ->
            ImageRetrieval.getURLImage(line, "data/categorized/GCC_categorized_images")
        }
    }
    catch (e: IOException) {
        e.stackTrace
    }


}