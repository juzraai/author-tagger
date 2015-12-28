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

package hu.juranyi.zsolt.jauthortagger.input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.model.JavaFiles;
import hu.juranyi.zsolt.jauthortagger.util.DirectoryFilter;
import hu.juranyi.zsolt.jauthortagger.util.JavaFileFilter;
import hu.juranyi.zsolt.jauthortagger.util.Log;

/**
 * Utility for enumerating <code>.java</code> files in a given directory
 * recursively. It produces a <code>JavaFiles</code> object, which contains
 * <code>JavaFile</code> objects with their <code>file</code> field filled in.
 *
 * @author Zsolt Jurányi
 * @see JavaFile
 *
 */
public class JavaFileEnumerator {

	private static final Logger LOG = Log.forClass(JavaFileEnumerator.class);

	/**
	 * <p>
	 * Enumerates <code>.java</code> files in the given directory recursively
	 * and return a <code>JavaFiles</code> object. The <code>file</code> fields
	 * of the embedded <code>JavaFile</code> objects will be filled in with the
	 * appropriate <code>File</code> object pointing to the <code>.java</code>
	 * file.
	 * </p>
	 * <p>
	 * This method is the public entry point for the recursive algorithm
	 * implemented by <code>enumerateJavaFilesImpl</code> method.
	 * </p>
	 *
	 * @param dir
	 *            The directory to be searched for <code>.java</code> files.
	 * @return The found <code>.java</code> files as a <code>JavaFiles</code>
	 *         object.
	 * @see #enumerateJavaFilesImpl(File)
	 * @see JavaFile
	 * @see JavaFiles
	 * @see JavaFileFilter
	 * @see DirectoryFilter
	 */
	public JavaFiles enumerateJavaFiles(File dir) {
		if (!dir.exists() || !dir.isDirectory()) {
			LOG.error("Something's wrong, it is not an existing directory: {}", dir.getAbsolutePath());
			return new JavaFiles();
		}
		return enumerateJavaFilesImpl(dir);
	}

	/**
	 * Enumerates <code>.java</code> files in the given directory recursively
	 * and return a <code>JavaFiles</code> object. The <code>file</code> fields
	 * of the embedded <code>JavaFile</code> objects will be filled in with the
	 * appropriate <code>File</code> object pointing to the <code>.java</code>
	 * file.
	 *
	 * @return The found <code>.java</code> files as a <code>JavaFiles</code>
	 *         object.
	 * @see JavaFile
	 * @see JavaFiles
	 * @see JavaFileFilter
	 * @see DirectoryFilter
	 */
	protected JavaFiles enumerateJavaFilesImpl(File dir) {
		List<JavaFile> javaFiles = new ArrayList<JavaFile>();
		if (null != dir && dir.exists() && dir.isDirectory()) {
			LOG.trace("Enumerating .java files in directory: {}", dir.getAbsolutePath());
			for (File javaFile : dir.listFiles(new JavaFileFilter())) {
				LOG.trace("Found .java file: {}", javaFile.getName());
				javaFiles.add(new JavaFile(javaFile));
			}
			for (File subDir : dir.listFiles(new DirectoryFilter())) {
				javaFiles.addAll(enumerateJavaFilesImpl(subDir));
			}
		}
		return new JavaFiles(javaFiles);

	}

}
