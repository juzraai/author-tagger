JAuthorTagger
=============

**Re-edit your javadoc @author tags at project level! ;)**

---



Intro
-----

I faced the problem that I have dozens of classes in a project without proper `@author` tags, but I want to publish my code. So I have to put/modify `@author` tags in EVERY class.

I didn't find a solution with Google under 1 minute and a paperful of feature ideas came to my mind within 5 minutes, so I decided to implement a thing.

I made **JAuthorTagger** which solves the problem (at least for me :D): I give him a project directory with a project specific configuration, and this thing injects the given authors into all `*.java` files.



Features
--------

* easy-2-use, 1 argument CLI
* project specific configuration via a simple, minimalistic text file
* you can tell which classes were edited by which authors, or which authors edited which classes
* you can tell at class level whether to merge or overwrite existing author lists with new ones
* also given classes can be skipped
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
* Get the jar: (comes soon to GitHub! :))
* Write a configuration file (see below) for your project named `path/2/ur/project/.authors`
* Run the thingy: `java -jar author-tagger.jar path/2/ur/project`

**JAuthorTagger** will automatically create a **backup** of every file, by adding `.at-save` suffix to the original filename. Previous backups will be overwritten.


### No-backup mode

If you provide "nobackup" as a 2nd command line argument:
* no backup will be created
* also, previous backups will be deleted


### Test mode

The program has a test mode which is a twisted behaviour. If you provide "test" as the 2nd argument:
* your original `.java` files will NOT be modified
* new files will be created instead, with `.at-test` suffix in their filename


### Restore

If you used the default backuping mode and you don't like the result, you can revert the changes by calling **JAuthorTagger** with "restore" as the 2nd argument.



Further ideas
-------------

* [in progress] diff report, maybe in a sexy HTML format - to see all modifications in one place
* smarter logging
* smarter algorithm: filename contains type name -> so we can look for declaration with type name
* manage `package-info.java` files too
* it will be more useful as a Maven plugin
* and/or as an Eclipse plugin with a nice GUI