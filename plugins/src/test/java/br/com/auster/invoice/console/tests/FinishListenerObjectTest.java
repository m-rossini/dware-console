package br.com.auster.invoice.console.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.com.auster.dware.console.listener.RequestFinished;

public class FinishListenerObjectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			
			RequestFinished obj = new RequestFinished();			
			obj.setMessage(null);
			obj.addFilename(null, null);
			
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(File.createTempFile("tmp", ".obj")) );
			oos.writeObject(obj);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
