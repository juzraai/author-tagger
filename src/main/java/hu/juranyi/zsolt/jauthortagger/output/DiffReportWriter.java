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

package hu.juranyi.zsolt.jauthortagger.output;

import static hu.juranyi.zsolt.jauthortagger.model.Filenames.DIFF_REPORT_TEMPLATE;
import static hu.juranyi.zsolt.jauthortagger.model.Filenames.diffReportOf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.model.BackupMode;
import hu.juranyi.zsolt.jauthortagger.model.Filenames;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.util.DiffCalculator;
import hu.juranyi.zsolt.jauthortagger.util.Log;

/**
 * Used for generating the sexy diff report. Basically this class only receives
 * diff data and calls <i>Apache Velocity</i> to output the HTML file using the
 * template defined by <code>src/main/resources/authors-diff-report.vm</code>.
 * The most important parameter of this class is the list of
 * <code>JavaFile</code>, they should contain the calculated diff result.
 *
 * @author Zsolt Jurányi
 * @see DiffCalculator
 * @see JavaFile
 *
 */
public class DiffReportWriter {

	private static final Logger LOG = Log.forClass(DiffReportWriter.class);

	static {
		Velocity.setProperty("resource.loader", "classpath");
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.init();
	}

	private final File projectDir;
	private final BackupMode backupMode;
	private final List<JavaFile> javaFiles;

	/**
	 * Creates an instance.
	 *
	 * @param projectDir
	 *            - The project directory.
	 * @param backupMode
	 *            - The backup mode.
	 * @param javaFiles
	 *            - The <code>JavaFile</code> objects with calculated diffs
	 *            inside.
	 * @see BackupMode
	 * @see DiffCalculator
	 * @see JavaFile
	 */
	public DiffReportWriter(File projectDir, BackupMode backupMode, List<JavaFile> javaFiles) {
		this.projectDir = projectDir;
		this.backupMode = backupMode;
		this.javaFiles = javaFiles;
	}

	/**
	 * Returns the backup mode.
	 *
	 * @return The backup mode.
	 */
	public BackupMode getBackupMode() {
		return backupMode;
	}

	/**
	 * Returns the <code>JavaFile</code> objects.
	 *
	 * @return The <code>JavaFile</code> objects.
	 */
	public List<JavaFile> getJavaFiles() {
		return javaFiles;
	}

	/**
	 * Returns the project directory.
	 *
	 * @return The project directory.
	 */
	public File getProjectDir() {
		return projectDir;
	}

	/**
	 * Does the real thing: builds up the <code>VelocityContext</code> by
	 * putting in all fields an the timestamp, then kindly asks <i>Velocity</i>
	 * to merge the template with the values and spit out the HTML file into the
	 * project directory.
	 *
	 * @see Filenames#diffReportOf(File)
	 */
	public void writeDiffReport() {
		File outputFile = diffReportOf(projectDir);

		VelocityContext vc = new VelocityContext();
		vc.put("timestamp", new SimpleDateFormat("yyyy-MM-dd @ HH:mm.ss").format(new Date()));
		vc.put("projectDir", projectDir.getAbsolutePath());
		vc.put("backupMode", backupMode);
		vc.put("javaFiles", javaFiles);

		try (Writer w = new OutputStreamWriter(new FileOutputStream(outputFile), Charset.forName("UTF-8"))) {
			Velocity.mergeTemplate(DIFF_REPORT_TEMPLATE, "UTF-8", vc, w);
			LOG.info("Report generated into file: {}", outputFile.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("Error when writing diff report", e);
		}
	}

}
