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

/**
 * Model class representing a <code>.java</code> file. It stores a
 * <code>File</code> object which points to the physical file, and some fields
 * that will be modified by the analyzer and the configuration. It will contain
 * the existing and new authors, the tagging mode and the index of the type
 * declaration's start line in the <code>.java</code> file. The default tagging
 * mode is <code>MERGE</code>.
 *
 * @author Zsolt Jurányi
 * @see AuthorTaggerConfig
 * @see AuthorTaggingMode
 * @see JavaFileAnalyzer
 *
 */
public class JavaFile {

	private final File file;
	private String typeName;
	private AuthorTaggingMode taggingMode = AuthorTaggingMode.MERGE;
	private final List<String> oldAuthors = new ArrayList<String>();
	private final List<String> newAuthors = new ArrayList<String>();
	private int typeDeclarationStartLine = -1;
	private DiffResult diffResult; // TODO doc

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
	 * Returns the list of new authors (authors to be added by the tagger).
	 *
	 * @return The list of new authors (authors to be added by the tagger).
	 */
	public List<String> getNewAuthors() {
		return newAuthors;
	}

	/**
	 * Returns the list of old authors (authors existing in the
	 * <code>.java</code> file).
	 *
	 * @return The list of old authors (authors existing in the
	 *         <code>.java</code> file).
	 */
	public List<String> getOldAuthors() {
		return oldAuthors;
	}

	/**
	 * Returns the tagging mode. The default value is <code>MERGE</code>.
	 *
	 * @return The tagging mode.
	 * @see AuthorTaggingMode
	 */
	public AuthorTaggingMode getTaggingMode() {
		return taggingMode;
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

	public void setDiffResult(DiffResult diffResult) {
		this.diffResult = diffResult;
	}

	/**
	 * Sets the tagging mode.
	 *
	 * @param taggingMode
	 *            The new tagging mode.
	 * @see AuthorTaggingMode
	 */
	public void setTaggingMode(AuthorTaggingMode taggingMode) {
		this.taggingMode = taggingMode;
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
		return "JavaFile [taggingMode=" + taggingMode + ", typeName=" + typeName + ", typeDeclarationStartLine="
				+ typeDeclarationStartLine + "]";
	}

}
