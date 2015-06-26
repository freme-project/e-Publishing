package eu.freme.eservices.epublishing;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class Unzipper {

    public static void unzip(ZipInputStream zis, String outputFolder) throws IOException {
        //create output directory is not exists
        File folder = new File(outputFolder);

        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Cannot create directory " + folder);
        }

        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {
            String fileName = ze.getName();
            File newFile = new File(outputFolder, fileName);

            System.out.println("file unzip : " + newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            File parentDir = newFile.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("Cannot create directory " + newFile.getParent());
            }

            if (ze.isDirectory()) {
                newFile.mkdirs();
            } else {
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    IOUtils.copyLarge(zis, fos);
                }
            }
            ze = zis.getNextEntry();
        }
        zis.closeEntry();
    }
}
