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
