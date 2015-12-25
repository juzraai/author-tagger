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

import difflib.DiffUtils;
import difflib.Patch;
import hu.juranyi.zsolt.jauthortagger.model.DiffResult;

public class DiffCalculator {

	private File originalFile;
	private File modifiedFile;
	private List<String> originalContent;
	private List<String> modifiedContent;
	private Patch<String> patch;
	private final List<String> unifiedDiff = new ArrayList<String>();

	public DiffCalculator(File originalFile, File modifiedFile, List<String> originalContent,
			List<String> modifiedContent) {
		this.originalFile = originalFile;
		this.modifiedFile = modifiedFile;
		this.originalContent = originalContent;
		this.modifiedContent = modifiedContent;
	}

	public DiffResult calculateDiff() {
		this.patch = DiffUtils.diff(originalContent, modifiedContent);
		this.unifiedDiff.clear();
		this.unifiedDiff.addAll(DiffUtils.generateUnifiedDiff(originalFile.getName(), modifiedFile.getName(),
				originalContent, patch, 3));
		return new DiffResult(patch, unifiedDiff);

	}

	public List<String> getModifiedContent() {
		return modifiedContent;
	}

	public File getModifiedFile() {
		return modifiedFile;
	}

	public List<String> getOriginalContent() {
		return originalContent;
	}

	public File getOriginalFile() {
		return originalFile;
	}

	public Patch<String> getPatch() {
		return patch;
	}

	public List<String> getUnifiedDiff() {
		return unifiedDiff;
	}

	public void setModifiedContent(List<String> modifiedContent) {
		this.modifiedContent = modifiedContent;
	}

	public void setModifiedFile(File modifiedFile) {
		this.modifiedFile = modifiedFile;
	}

	public void setOriginalContent(List<String> originalContent) {
		this.originalContent = originalContent;
	}

	public void setOriginalFile(File originalFile) {
		this.originalFile = originalFile;
	}

	public void setPatch(Patch<String> patch) {
		this.patch = patch;
	}

}
