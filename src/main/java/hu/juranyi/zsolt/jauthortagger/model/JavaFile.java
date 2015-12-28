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

package hu.juranyi.zsolt.jauthortagger.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hu.juranyi.zsolt.jauthortagger.input.AuthorTaggerConfig;
import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.output.DiffReportWriter;

/**
 * Model class representing a <code>.java</code> file. It stores a
 * <code>File</code> object which points to the physical file, and some fields
 * that will be modified by the analyzer and the configuration. It will contain
 * the authors, the index of the type declaration's start line in the
 * <code>.java</code> file, and the calculated diff for report generation.
 *
 * @author Zsolt Jurányi
 * @see AuthorTaggerConfig
 * @see DiffReportWriter
 * @see JavaFileAnalyzer
 *
 */
public class JavaFile {

	private final File file;
	private String typeName;
	private final List<String> authors = new ArrayList<String>();
	private int typeDeclarationStartLine = -1;
	private DiffResult diffResult;

	/**
	 * Creates an instance.
	 *
	 * @param file
	 *            A <code>File</code> object pointing to a <code>.java</code>
	 *            file.
	 */
	public JavaFile(File file) {
		this.file = file;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JavaFile other = (JavaFile) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	/**
	 * Returns the list of authors.
	 *
	 * @return The list of authors.
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * Returns the diff result.
	 *
	 * @return The diff result.
	 * @see DiffResult
	 */
	public DiffResult getDiffResult() {
		return diffResult;
	}

	/**
	 * Returns the <code>File</code> object pointing to the <code>.java</code>
	 * file.
	 *
	 * @return The <code>File</code> object pointing to the <code>.java</code>
	 *         file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the line index of the type declaration's first line. The default
	 * value is -1.
	 *
	 * @return The line index of the type declaration's first line.
	 */
	public int getTypeDeclarationStartLine() {
		return typeDeclarationStartLine;
	}

	/**
	 * Returns the full name of the public type declared in the
	 * <code>.java</code> file.
	 *
	 * @return The full name of the public type declared in the
	 *         <code>.java</code> file.
	 */
	public String getTypeName() {
		return typeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	/**
	 * Sets the diff result.
	 *
	 * @param diffResult
	 *            The diff result.
	 * @see DiffResult
	 */
	public void setDiffResult(DiffResult diffResult) {
		this.diffResult = diffResult;
	}

	/**
	 * Sets the line index of the type declaration's first line.
	 *
	 * @param typeDeclarationStartLine
	 *            The line index of the type declaration's first line.
	 */
	public void setTypeDeclarationStartLine(int typeDeclarationStartLine) {
		this.typeDeclarationStartLine = typeDeclarationStartLine;
	}

	/**
	 * Sets the full name of the public type declared in the <code>.java</code>
	 * file.
	 *
	 * @param typeName
	 *            The full name of the public type declared in the
	 *            <code>.java</code> file.
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "JavaFile [typeName=" + typeName + ", authors=" + authors + ", typeDeclarationStartLine="
				+ typeDeclarationStartLine + "]";
	}

}
