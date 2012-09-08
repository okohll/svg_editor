Background
===========

This is a vector (SVG) editor, a fork of the GLIPS project, which was last updated in 2007.

It is being updated to
1. compile and run correctly with modern libraries and Java versions.
2. add a plugin architecture, for use initially with certain private plugins that I'm creating separately. It's not expected that this will be of more general use in the short term.

DONE
=====

This is a relatively large codebase so superficial changes and 'low hanging fruit' have been made so far.

* Third party libraries updated to their newest versions, notably the Apache Batik SVG library. Calling code has been altered so it compiles.
* Some code cleanup, notably there were a few hundred instances of raw exceptions being swallowed, e.g.
    try {
      code...
    } catch (Exception e) {
    }
  which would make troubleshooting hard. These sections now at least log the exceptions. In some cases unnecessary try/catch blocks have been entirely removed.
  This behaviour will hopefully be able to be further improved through a better understanding of methods in future.
* The codebase has been run through FindBugs and many issues fixed.
*  Large sections of code (packages ans classes) dealing with specialised Industrial Control Procedures have been removed, leaving just the generic image editing functions.
   There are probably a few remaining classes that can be removed.
* The File Open dialog has been tweaked to make it function. There are still some issues.

Java version 7 is now required for compilation.

TODO
=====

The main item on the todo list is to fix compatibility with all valid SVG files. The app currently opens and displays some but not others. Sometimes altering the SVG header helps.

I am looking for help in working on this issue. Patches would be most welcome.

You may notice that there is a basic plugin architecture which I'm sure could be better implemented, however this is currently work in progress for my use only.
Development should concentrate on the basic image opening and editing features first.

There is also further scope for code cleanup and modernisation. As one simple 'micro' level example, perhaps some Iterators can be replaced with for loops.