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
import java.util.List;

import difflib.DiffUtils;
import difflib.Patch;
import hu.juranyi.zsolt.jauthortagger.model.DiffResult;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;

/**
 * This class is used to calculate diff between the <code>.java</code> file's
 * original and modified versions. It uses the <i>Java DiffUtils</i> library and
 * produces <code>DiffResult</code> object to be stored in a
 * <code>JavaFile</code>, which contains the <code>Patch</code> object and the
 * unified diff as well.
 *
 * @author Zsolt Jurányi
 * @see DiffResult
 * @see JavaFile
 */
public class DiffCalculator {

	private final File originalFile;
	private final File modifiedFile;
	private final List<String> originalContent;
	private final List<String> modifiedContent;

	/**
	 * Creates an instance.
	 *
	 * @param originalFile
	 *            - The original file's name to use in the unified diff.
	 * @param modifiedFile
	 *            - The modified file's name to use in the unified diff.
	 * @param originalContent
	 *            - Lines of the original content.
	 * @param modifiedContent
	 *            - Lines of the modified content.
	 */
	public DiffCalculator(File originalFile, File modifiedFile, List<String> originalContent,
			List<String> modifiedContent) {
		this.originalFile = originalFile;
		this.modifiedFile = modifiedFile;
		this.originalContent = originalContent;
		this.modifiedContent = modifiedContent;
	}

	/**
	 * Calculates the diff and stores <code>difflib.Patch</code> object and
	 * lines of the unified diff in a <code>DiffResult</code> object.
	 *
	 * @return A <code>DiffResult</code> object containing a
	 *         <code>difflib.Patch</code> object and lines of the unified diff.
	 */
	public DiffResult calculateDiff() {
		Patch<String> patch = DiffUtils.diff(originalContent, modifiedContent);
		List<String> unifiedDiff = DiffUtils.generateUnifiedDiff(originalFile.getName(), modifiedFile.getName(),
				originalContent, patch, 3);
		return new DiffResult(patch, unifiedDiff);

	}

	/**
	 * Returns the lines of the modified content.
	 *
	 * @return The lines of the modified content.
	 */
	public List<String> getModifiedContent() {
		return modifiedContent;
	}

	/**
	 * Returns the modified file's name as a <code>File</code> object.
	 *
	 * @return The modified file's name as a <code>File</code> object.
	 */
	public File getModifiedFile() {
		return modifiedFile;
	}

	/**
	 * Returns the lines of the original content.
	 *
	 * @return The lines of the original content.
	 */
	public List<String> getOriginalContent() {
		return originalContent;
	}

	/**
	 * Returns the original file's name as a <code>File</code> object.
	 *
	 * @return The original file's name as a <code>File</code> object.
	 */
	public File getOriginalFile() {
		return originalFile;
	}

}
