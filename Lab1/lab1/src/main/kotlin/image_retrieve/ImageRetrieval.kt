package image_retrieve

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URL

/**
 * @author djyuhn
 * 2/19/2019
 */
object ImageRetrieval  {

    fun getURLImage(imageURL: String, destination: String) {
        try {
            val url = URL(imageURL)
            val filename = url.file
            val file = File(destination + "/images/" + filename.substring(filename.lastIndexOf("/")))

            FileUtils.copyURLToFile(url, file)
        }
        catch (e: IOException){
            e.stackTrace
        }

    }

}