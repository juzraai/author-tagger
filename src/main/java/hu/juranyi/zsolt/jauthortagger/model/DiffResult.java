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

import java.util.List;

import difflib.Patch;

/**
 * A simple POJO to store the calculated diff result of the original and
 * modified <code>.java</code> files. It is needed to generate a diff report to
 * the user.
 *
 * @author Zsolt Jurányi
 *
 */
public class DiffResult {

	private final Patch<String> patch;
	private final List<String> unifiedDiff;

	/**
	 * Creates an instance.
	 *
	 * @param patch
	 *            - A <code>difflib.Patch</code> object which stores deltas.
	 * @param unifiedDiff
	 *            - Lines of a unified diff generated from the patch.
	 */
	public DiffResult(Patch<String> patch, List<String> unifiedDiff) {
		this.patch = patch;
		this.unifiedDiff = unifiedDiff;
	}

	/**
	 * Returns the <code>difflib.Patch</code> object which stores deltas.
	 *
	 * @return The <code>difflib.Patch</code> object which stores deltas.
	 */
	public Patch<String> getPatch() {
		return patch;
	}

	/**
	 * Returns the lines of the unified diff generated from the patch.
	 *
	 * @return The lines of the unified diff generated from the patch.
	 */
	public List<String> getUnifiedDiff() {
		return unifiedDiff;
	}

}
