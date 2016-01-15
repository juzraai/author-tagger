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

package hu.juranyi.zsolt.jauthortagger.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.util.TestUtils;

public class AuthorTaggerConfigTest { // TODO

	// Should test:
	// - cfg parsing
	// - $ section: !skip, -, +
	// - @ section: -, +
	// - no duplicates in authors
	// - test on JavaFile objects, no need to write/analyze files
	// - only the conf file - but we should ease it somehow... parse list<str>

	@BeforeClass
	@AfterClass
	public static void cleanup() {
		TestUtils.deleteTestDir();
	}

	@Test
	public void test() {

	}

}
