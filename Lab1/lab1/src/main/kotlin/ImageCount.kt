import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * @author djyuhn
 * 2/25/2019
 */

fun main(args:Array<String>) {

    val imageDirectoryPath = "data/categorized/flickr_categorized_images"
    val dataCategorizedPath = "data/categorized/"

    val dir = File(imageDirectoryPath)
    val dirFiles = dir.listFiles()
    val stringBuilder = StringBuilder()

    if(!dirFiles.isEmpty()) {
        for (file: File in dirFiles) {
            val categoryFiles = file.listFiles()
            stringBuilder.append(file.name + "\t" + categoryFiles.size + "\n")
        }
    }

    val categorizedCOCOImageCount = BufferedWriter(FileWriter(dataCategorizedPath + "Flikr_Image_Categorized_Count.txt"))

    val splitBuilder = stringBuilder.toString().split("\n")

    splitBuilder.forEach { line ->
        categorizedCOCOImageCount.append(line).append("\n")
    }

    categorizedCOCOImageCount.close()

}