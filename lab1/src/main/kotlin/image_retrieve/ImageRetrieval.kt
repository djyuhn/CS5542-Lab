package image_retrieve

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

/**
 * @author djyuhn
 * 2/19/2019
 */
object ImageRetrieval  {

    fun getURLImage(imageURL: String) {
        try {
            val url = URL(imageURL)
            val image: BufferedImage = ImageIO.read(url)
            val fileName: String = url.file
            val destinationName: String = "./images" + fileName.substring(fileName.lastIndexOf("/"))

            ImageIO.write(image, "png", File(destinationName))
        }

        catch(e: IOException) {
            e.printStackTrace()
        }

    }

}