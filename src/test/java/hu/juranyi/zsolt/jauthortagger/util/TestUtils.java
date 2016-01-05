/*
 * Copyright 2015 Zsolt Jurányi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.juranyi.zsolt.jauthortagger.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Utility methods to be used in JUnit test cases.
 *
 * @author Zsolt Jurányi
 *
 */
public class TestUtils {

	public static final File TEST_DIR = new File("test-files");

	public static File createEmptyFile(String fn) {
		File outFile = new File(TEST_DIR, fn);
		FileWriter w = null;
		try {
			File parent = outFile.getParentFile();
			if (null != parent && !parent.exists()) {
				parent.mkdirs();
			}
			w = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != w) {
				try {
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return outFile;
	}

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
			is = resource(resFn);
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

	public static InputStream resource(String name) {
		return TestUtils.class.getClassLoader().getResourceAsStream(name);
	}

}
