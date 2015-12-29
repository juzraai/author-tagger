package hu.juranyi.zsolt.jauthortagger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.util.TestUtils;

public class JavaFileAnalyzerTest {

	// TODO analyzePackageInfo

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
	public void analyzeClassInRootPackage() {
		JavaFile javaFile = analyze("ClassInRootPackage");
		assertEquals("ClassInRootPackage", javaFile.getTypeName());
		assertEquals(4, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

	@Test
	public void analyzeClassWithJavadoc() {
		JavaFile javaFile = analyze("ClassWithJavadoc");
		assertEquals("name.of.the.sample.package.ClassWithJavadoc", javaFile.getTypeName());
		assertEquals(15, javaFile.getTypeDeclarationStartLine());
		assertEquals(Arrays.asList("Zsolt Jurányi", "Someone Else"), javaFile.getAuthors());
	}

	@Test
	public void analyzeClassWithoutJavadoc() {
		JavaFile javaFile = analyze("ClassWithoutJavadoc");
		assertEquals("name.of.the.sample.package.ClassWithoutJavadoc", javaFile.getTypeName());
		assertEquals(9, javaFile.getTypeDeclarationStartLine());
		assertTrue(javaFile.getAuthors().isEmpty());
	}

}
