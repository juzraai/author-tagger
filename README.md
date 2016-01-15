JAuthorTagger <img src="https://img.shields.io/badge/version-1.1.0-blue.svg"/> <img alt="Build Status Images" src="https://travis-ci.org/juzraai/author-tagger.svg">
=============

**Re-edit your javadoc @author tags at project level! ;)**

---



Intro
-----

I faced the problem that I have dozens of classes in a project without proper `@author` tags, but I want to publish my code. So I have to put/modify `@author` tags in EVERY class.

I didn't find a solution with Google under 1 minute and a paperful of feature ideas came to my mind within 5 minutes, so I decided to implement a thing.

I made *JAuthorTagger* which solves the problem (at least for me :D): I give him a project directory with a project specific configuration, and this thing injects the given authors into all `*.java` files.



Features
--------

* easy-2-use, 1 argument CLI
* project specific configuration via a simple, minimalistic text file
* you can tell which classes were edited by which authors, or which authors edited which classes
* you can remove authors from classes by pattern
* you can skip classes
* handles `package-info.java` files too
* creates backup automatically by default, but you can turn it off
* can restore previous version from backup
* test mode: no modification, creates new files with modified author lists instead
* generates a sexy HTML diff report to show you what had been changed



Limitations
-----------

The algorithm is really simple, it assumes that your files are well-formatted:

* the package declaration's line should start with the keyword `package` (without leading inline comment)
* the authors should be in one line each in this format: `" * @author name" `
* the first type declaration in the file must be the public one
* also the `public` must be at the beginning of the line
* the algorithm does not check if the lines are in the proper block or outside of a multiline comment, it will pick the first line that matches the appropriate pattern



Usage
-----

1. Install JRE 1.7+
2. [Get the JAR](https://github.com/juzraai/author-tagger/releases)
3. Write a configuration file (see below) for your project named `path/2/ur/project/.authors`
4. Run the thingy: `java -jar author-tagger.jar path/2/ur/project`

*JAuthorTagger* will automatically create a **backup** of every file, by adding `.at-save` suffix to the original filename. Previous backups will be overwritten.


### No-backup mode

If you provide `nobackup` as a 2nd command line argument:

* no backup will be created
* also, previous backups will be deleted


### Test mode

The program has a test mode which is a twisted behaviour. If you provide `test` as the 2nd argument:

* your original `.java` files will NOT be modified
* new files will be created instead, with `.at-test` suffix in their filename


### Restore

If you used the default backup mode and you don't like the result, you can revert the changes by calling *JAuthorTagger* with `restore` as the 2nd argument.



Configuration
-------------

* the configuration file is a simple text file which will be interpreted line by line, sequentially
* it must be placed in the project directory with the name `.authors`
* it must have UTF-8 encoding without BOM
* configuration lines are those which match the pattern: `[any whitespace] ACTION [any whitespace] PARAMETER`
* lines that not match the pattern will be skipped, so you can write simple text around configuration lines
* whitespaces are optional, they will be thrown away when parsing
* available actions:
  * `$` (**$**elect)
  * `@` (**@**uthor)
  * `!` (special operation)
  * `-` (deletion)
  * `+` (addition)
* `$` - Starts a new section. The parameter must be a class name filter (see `ClassNameFilter`'s doc). It selects your `.java` files, and the following operations (special, del, add) will be performed on them.
* `@` - Starts a new section. The parameter must be an author name. The following operations (del, add) will affect this author.
* `!` - There is only one special action, `skip`, it can be used in `$` sections. The skip action tells *JAuthorTagger* to forget the selected classes, do not perform any operation on them.
* `-` - Used in a `$` section, the parameter must be an author name filter (see `SimpleStringFilter`'s doc), and means that matching authors in the selected classes will be removed. Used in a `@` section, the parameter must be a class name filter, and means that the selected author will be removed from matching classes.
* `+` - Used in a `$` section, the parameter must be an author name: it will be added to the selected classes. Used in a `@` section, the parameter must be a class name filter, and means that the given author will be added to matching classes.

Sample configuration file:
```
Selecting my classes:
$hu.juranyi.zsolt.**

	Deleting any other author, because now I want to overwrite:
	-*

	And adding myself:
	+Zsolt Jurányi
```

This is idenctical to the previous one:
```
$hu.juranyi.zsolt.**
	-*

@ Zsolt Jurányi
	+hu.juranyi.zsolt.**
```

If you want to skip classes from the whole procedure:
```
$com.classes.to.skip.**
	!skip

Or just by classname:
$VendorThing*
	!skip
```

Remember, indenting and separating action char from parameter is really up to you, it isn't necessary for **JAuthorTagger**.



Further ideas
-------------

* smarter logging
* smarter algorithm: filename contains type name -> so we can look for declaration with type name
* it will be more useful as a Maven plugin
* and/or as an Eclipse plugin with a nice GUI