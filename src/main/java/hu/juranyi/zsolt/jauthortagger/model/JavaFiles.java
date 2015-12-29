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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;
import hu.juranyi.zsolt.jauthortagger.util.Log;
import hu.juranyi.zsolt.jauthortagger.util.SimpleStringFilter;

/**
 * Extends <code>ArrayList&lt;JavaFile&gt;</code> with some useful methods like
 * add/remove an author from some classes using a filter.
 *
 * @author Zsolt Jurányi
 *
 */
public class JavaFiles extends ArrayList<JavaFile> {

	private static final Logger LOG = Log.forClass(JavaFiles.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an empty list with an initial capacity of ten.
	 */
	public JavaFiles() {
		super();
	}

	/**
	 * Constructs a list containing the elements of the specified collection, in
	 * the order they are returned by the collection's iterator.
	 *
	 * @param c
	 *            - the collection whose elements are to be placed into this
	 *            list
	 */
	public JavaFiles(Collection<? extends JavaFile> c) {
		super(c);
	}

	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param initialCapacity
	 *            - the initial capacity of the list
	 */
	public JavaFiles(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Adds the given author to <code>JavaFile</code> objects that match the
	 * given class name filter. Uses <code>ClassNameFilter</code> to handle the
	 * filtering.
	 *
	 * @param classFilter
	 *            - Class name filter to apply on <code>JavaFile</code> objects.
	 * @param author
	 *            - Author to be added to the filtered <code>JavaFile</code>
	 *            objects.
	 * @see ClassNameFilter
	 */
	public void addAuthor(String classFilter, String author) {
		ClassNameFilter filter = new ClassNameFilter(classFilter);
		for (JavaFile javaFile : this) {
			if (filter.accept(javaFile.getTypeName())) {
				LOG.trace("{} >> {}", author, javaFile.getTypeName());
				if (!javaFile.getAuthors().contains(author)) {
					javaFile.getAuthors().add(author);
				}
			}
		}
	}

	/**
	 * Filters <code>JavaFile</code> objects with <code>ClassNameFilter</code>,
	 * and deletes their authors which match the given filter. Author filtering
	 * is done using <code>SimpleStringFilter</code>.
	 *
	 * @param classFilterStr
	 *            - Class name filter to apply on <code>JavaFile</code> objects.
	 * @param authorFilterStr
	 *            - Simple filter to apply on authors.
	 * @see ClassNameFilter
	 * @see SimpleStringFilter
	 */
	public void delAuthor(String classFilterStr, String authorFilterStr) {
		ClassNameFilter classFilter = new ClassNameFilter(classFilterStr);
		SimpleStringFilter authorFilter = new SimpleStringFilter(authorFilterStr);
		for (JavaFile javaFile : this) {
			if (classFilter.accept(javaFile.getTypeName())) {
				int i = 0;
				List<String> authors = javaFile.getAuthors();
				while (i < authors.size()) {
					String author = authors.get(i);
					if (authorFilter.accept(author)) {
						LOG.trace("{} << {}", author, javaFile.getTypeName());
						authors.remove(i);
					} else {
						i++;
					}
				}
			}
		}
	}

	/**
	 * Removes <code>JavaFile</code> objects that match the given class name
	 * filter.
	 *
	 * @param classFilter
	 *            - Class name filter to select <code>JavaFile</code> objects to
	 *            remove.
	 */
	public void skip(String classFilter) {
		ClassNameFilter filter = new ClassNameFilter(classFilter);
		int i = 0;
		while (i < size()) {
			JavaFile javaFile = get(i);
			if (filter.accept(javaFile.getTypeName())) {
				LOG.trace("DELETE :: {}", javaFile.getTypeName());
				remove(i);
			} else {
				i++;
			}
		}
	}

}
