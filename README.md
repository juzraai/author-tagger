JAuthorTagger
=============

**Re-edit your javadoc @author tags at project level! ;)**

---



Intro
-----

I faced the problem that I have dozens of classes in a project without proper `@author` tags, but I want to publish my code. So I have to put/modify `@author` tags in EVERY class.

I didn't find a solution with Google under 1 minute and a paperful of feature ideas came to my mind within 5 minutes, so I decided to implement a thing.

So I made **JAuthorTagger** which solves the problem (at least for me :D): I give him a project directory with a project specific configuration, and this thing injects the given authors into all `*.java` files.



Features
--------

* easy-2-use, 1 argument CLI
* project specific configuration via a simple, minimalistic text file
* customizable behaviour: you can tell which classes edited by which authors, or which authors edited which classes
* it can merge or overwrite existing author lists with new ones
* you can tell which classes should be skipped and also there's a test drive option (no modification)



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
* Get the jar: [link]
* Write a configuration file (see below) for your project named `path/2/ur/project/.authors`
* Run the thingy: `java -jar author-tagger.jar path/2/ur/project`

**Note:** I suggest you to backup/commit your code before this procedure, just to be safe. :)

Optionally you can put "test" at the end of the command, as an additional argument. This way **JAuthorTagger** will NOT modify your files, instead it will create a directory named "authors-test" inside your project and spit out the modified files there.



Further ideas
-------------

* manage `package-info.java` files too
* it will be more useful as a Maven plugin
* and/or as an Eclipse plugin with a nice GUI