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

public class DiffResult {

	private final Patch<String> patch;
	private final List<String> unifiedDiff;

	public DiffResult(Patch<String> patch, List<String> unifiedDiff) {
		this.patch = patch;
		this.unifiedDiff = unifiedDiff;
	}

	public Patch<String> getPatch() {
		return patch;
	}

	public List<String> getUnifiedDiff() {
		return unifiedDiff;
	}

}
