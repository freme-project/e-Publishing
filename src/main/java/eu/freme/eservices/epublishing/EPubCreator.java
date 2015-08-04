/**
 * Copyright (C) 2015 Deutsches Forschungszentrum für Künstliche Intelligenz (http://freme-project.eu)
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

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>Implementations of this interface should create a nice EPUB file!</p>
 *
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public interface EPubCreator {

	/**
	 * Called when a text file is found in the archive.
	 * @param name		The name of the file as stored in the archive.
	 * @param contents  The textual contents of the file. It assumes the text is encoded as UTF-8.
	 */
	void onText(final String name, final String contents) throws IOException;

	/**
	 * Called when a binary file is found in the archive OR if the file is not recognized as text.
	 * @param name      The name of the file as stored in the archive.
	 * @param contents  The binary contents of the file.
	 */
	void onBinary(final String name, final byte[] contents) throws IOException;

	/**
	 * Called when the end of the archive is reached. In other words: game over.
	 * @param out	    The book will be written to this stream when finished.
	 */
	void onEnd(final OutputStream out) throws IOException;
}
