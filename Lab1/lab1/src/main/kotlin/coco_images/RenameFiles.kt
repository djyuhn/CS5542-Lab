package coco_images

import java.io.File

/**
 * @author djyuhn
 * 2/25/2019
 */

fun main(args:Array<String>) {
    /**
     * Remove leading 0's from all filenames of COCO_ImageSet
     */

    val imageDirectoryPath = "C:\\Users\\djyuhn\\Downloads\\COCO_ImageSet"

    val dir = File(imageDirectoryPath)
    val dirFiles = dir.listFiles()

    if(!dirFiles.isEmpty()) {
        for (file: File in dirFiles) {

            for (char: Char in file.name) {
                if (char != file.name[0]) {
                    val index = file.name.indexOf(char)
                    val newFilename = file.name.substring(index)

                    val renameFile = File("$imageDirectoryPath/$newFilename")
                    file.renameTo(renameFile)

                    break
                }
            }

        }
    }
}
