/**
 * Copyright (C) ${project.inceptionYear} Felix Sasaki (Felix.Sasaki@dfki.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
