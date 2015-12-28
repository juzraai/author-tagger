package hu.juranyi.zsolt.jauthortagger.model;

import java.util.ArrayList;
import java.util.Collection;

import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;

public class JavaFiles extends ArrayList<JavaFile> {

	private static final long serialVersionUID = 1L;

	public JavaFiles() {
		super();
	}

	public JavaFiles(Collection<? extends JavaFile> c) {
		super(c);
	}

	public JavaFiles(int initialCapacity) {
		super(initialCapacity);
	}

	public void addAuthor(String classFilter, String author) {
		ClassNameFilter filter = new ClassNameFilter(classFilter);
		for (JavaFile javaFile : this) {
			if (filter.accept(javaFile.getTypeName())) {
				javaFile.getAuthors().add(author);
			}
		}
	}

	public void delAuthor(String classFilter, String authorFilter) {
		ClassNameFilter filter = new ClassNameFilter(classFilter);
		for (JavaFile javaFile : this) {
			if (filter.accept(javaFile.getTypeName())) {
				// TODO delete author matching author filter!
			}
		}
	}

	public void skip(String classFilter) {
		ClassNameFilter filter = new ClassNameFilter(classFilter);
		int i = 0;
		while (i < size()) {
			JavaFile javaFile = get(i);
			if (filter.accept(javaFile.getTypeName())) {
				remove(i);
			} else {
				i++;
			}
		}
	}

}
