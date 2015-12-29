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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.util.TestUtils;

/**
 * Tests the analyzer component on all source file structures. The main two are
 * classes and package-infos. Both can optionally contain annotations and doc
 * block with author tags, classes can have package declarations too.
 *
 * @author Zsolt Jurányi
 *
 */
public class JavaFileAnalyzerTest {

	// XXX would be nice to generate test files with Velocity ;)

	@AfterClass
	public static void cleanup() {
		TestUtils.deleteTestDir();
	}

	private JavaFile analyze(String testClassName) {
		JavaFile javaFile = new JavaFile(
				TestUtils.exportResourceFile(testClassName + ".java_", testClassName + ".java"));
		new JavaFileAnalyzer().analyzeJavaFile(javaFile);
		return javaFile;
	}

	@Test
	public void analyzeClass() {
		JavaFile javaFile = analyze("Class");
		assertEquals("Class", javaFile.getTypeName());
		assertEquals(5, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzeClassA() {
		JavaFile javaFile = analyze("ClassA");
		assertEquals("ClassA", javaFile.getTypeName());
		assertEquals(5, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzeClassAD() {
		JavaFile javaFile = analyze("ClassAD");
		assertEquals("ClassAD", javaFile.getTypeName());
		assertEquals(10, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

	@Test
	public void analyzeClassADP() {
		JavaFile javaFile = analyze("ClassADP");
		assertEquals("name.of.the.sample.package.ClassADP", javaFile.getTypeName());
		assertEquals(12, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

	@Test
	public void analyzeClassAP() {
		JavaFile javaFile = analyze("ClassAP");
		assertEquals("name.of.the.sample.package.ClassAP", javaFile.getTypeName());
		assertEquals(7, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzeClassD() {
		JavaFile javaFile = analyze("ClassD");
		assertEquals("ClassD", javaFile.getTypeName());
		assertEquals(10, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

	@Test
	public void analyzeClassDP() {
		JavaFile javaFile = analyze("ClassDP");
		assertEquals("name.of.the.sample.package.ClassDP", javaFile.getTypeName());
		assertEquals(12, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

	@Test
	public void analyzeClassP() {
		JavaFile javaFile = analyze("ClassP");
		assertEquals("name.of.the.sample.package.ClassP", javaFile.getTypeName());
		assertEquals(7, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzePkgInf() {
		JavaFile javaFile = analyze("PkgInf");
		assertEquals("name.of.the.sample.package.PkgInf", javaFile.getTypeName());
		assertEquals(5, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzePkgInfA() {
		JavaFile javaFile = analyze("PkgInfA");
		assertEquals("name.of.the.sample.package.PkgInfA", javaFile.getTypeName());
		assertEquals(5, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzePkgInfAD() {
		JavaFile javaFile = analyze("PkgInfAD");
		assertEquals("name.of.the.sample.package.PkgInfAD", javaFile.getTypeName());
		assertEquals(10, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

	@Test
	public void analyzePkgInfD() {
		JavaFile javaFile = analyze("PkgInfD");
		assertEquals("name.of.the.sample.package.PkgInfD", javaFile.getTypeName());
		assertEquals(10, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

}
