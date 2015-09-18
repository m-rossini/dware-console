package br.com.auster.invoice.console.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import br.com.auster.common.io.CompressUtils;

public class CompressTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			File f2 = new File("/tmp/test-1.entry");
			File f3 = new File("/tmp/test-2.entry");
			File f4 = new File("/tmp/test-3.entry");
			
			ArrayList list = new ArrayList();
			list.add(f2);
			list.add(f3);
			list.add(f4);
			File zipFile = CompressUtils.createZIPBundle(list, "/tmp/tmp.zip");
			
			FileOutputStream out = new FileOutputStream(f3);
			out.write(System.getProperty("line.separator").getBytes());
			out.write("Some other thing".getBytes());
			out.close();
			
			list.clear();
			list.add(f3);
			File zip2 = CompressUtils.createZIPBundle(list, "/tmp/tmp.zip");
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
