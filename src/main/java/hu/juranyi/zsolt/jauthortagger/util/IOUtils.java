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

public class IOUtils {

	public static List<String> fileToStringList(File f) {
		List<String> lines = new ArrayList<String>();
		if (null != f && f.exists() && f.isFile()) {
			try {
				Scanner s = new Scanner(f, "UTF-8");
				while (s.hasNextLine()) {
					lines.add(s.nextLine());
				}
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
}
