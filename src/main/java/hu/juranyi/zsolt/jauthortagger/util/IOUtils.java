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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Store of I/O related methods.
 *
 * @author Zsolt Jurányi
 *
 */
public class IOUtils {

	/**
	 * Reads an UTF-8 encoded text file and returns its lines as list of
	 * <code>String</code> objects.
	 *
	 * @param f
	 *            - The file to read.
	 * @return The list of lines.
	 */
	public static List<String> fileToStringList(File f) {
		List<String> lines = new ArrayList<String>();
		if (null != f && f.exists() && f.isFile()) {
			Scanner s = null;
			try {
				s = new Scanner(f, "UTF-8");
				while (s.hasNextLine()) {
					lines.add(s.nextLine());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != s) {
					s.close();
				}
			}
		}
		return lines;
	}
}
