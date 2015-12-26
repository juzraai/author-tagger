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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import hu.juranyi.zsolt.jauthortagger.util.Log;

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

	public DiffReportWriter(File projectDir, BackupMode backupMode, List<JavaFile> javaFiles) {
		this.projectDir = projectDir;
		this.backupMode = backupMode;
		this.javaFiles = javaFiles;
	}

	public BackupMode getBackupMode() {
		return backupMode;
	}

	public List<JavaFile> getJavaFiles() {
		return javaFiles;
	}

	public File getProjectDir() {
		return projectDir;
	}

	public void writeDiffReport() {
		File outputFile = new File(projectDir, Filenames.DIFF_REPORT_FILE);

		VelocityContext vc = new VelocityContext();
		vc.put("timestamp", new SimpleDateFormat("yyyy-MM-dd @ HH:mm.ss").format(new Date()));
		vc.put("projectDir", projectDir.getAbsolutePath());
		vc.put("backupMode", backupMode);
		vc.put("javaFiles", javaFiles);

		// TODO unifiedDiff lines should be HTML encoded

		FileWriter w = null;
		try {
			w = new FileWriter(outputFile);
			Velocity.mergeTemplate("authors-diff-report.vm", "UTF-8", vc, w);
		} catch (IOException e) {
			LOG.error("Error when writing diff report", e);
		} finally {
			if (null != w) {
				try {
					w.close();
					LOG.info("Report generated into file: {}", outputFile.getAbsolutePath());
				} catch (IOException e) {
					LOG.error("Error when closing diff report", e);
				}
			}
		}
	}

}
