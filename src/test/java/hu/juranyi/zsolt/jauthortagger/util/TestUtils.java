package hu.juranyi.zsolt.jauthortagger.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TestUtils {

	private static final File TEST_DIR = new File("test-files");

	public static void deleteTestDir() {
		try {
			FileUtils.deleteDirectory(TEST_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File exportResourceFile(String resFn, String outFn) {
		File outFile = new File(TEST_DIR, outFn);
		InputStream is = null;
		OutputStream os = null;
		try {
			File parent = outFile.getParentFile();
			if (null != parent && !parent.exists()) {
				parent.mkdirs();
			}
			is = TestUtils.class.getClassLoader().getResourceAsStream(resFn);
			os = new FileOutputStream(outFile);
			IOUtils.copy(is, os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return outFile;
	}

}
