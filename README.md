1) Using Maven (my preference)
- Install Robocode in your preferred directory (not your project directory): java -jar robocode-1.9.3.3-setup.jar
- Create your project in your preferred directory
- Robocode libraries are not deployed to Maven central. Therefore, you will need to deploy them locally to the Maven repo like so (replace '@lib_directory' with the appropriate location):
``mvn install:install-file -Dfile=@lib_directory/codesize-1.1.jar -DartifactId=codesize  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/picocontainer-2.14.2.jar -DartifactId=picocontainer  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.battle-1.9.3.3.jar -DartifactId=robocode.battle  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.core-1.9.3.3.jar -DartifactId=robocode.core  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.host-1.9.3.3.jar -DartifactId=robocode.host  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.repository-1.9.3.3.jar -DartifactId=robocode.repository  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.sound-1.9.3.3.jar -DartifactId=robocode.sound  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.ui-1.9.3.3.jar -DartifactId=robocode.ui  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/roborumble.jar -DartifactId=roborumble  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.ui.editor-1.9.3.3.jar -DartifactId=robocode.ui.editor  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=@lib_directory/robocode.jar -DartifactId=robocode  -DgroupId=net.sourceforge.robocode -Dversion=1.9.3.3 -Dpackaging=jar -DgeneratePom=true
``
- Then just run the main class Robocode. That's it!
 
2) Without Maven
- Install Robocode in your preferred directory: java -jar robocode-1.9.3.3-setup.jar
- You will have to create you project in the "robots" directory present in the robocode installed directory
- Add the 'robocode.jar' as a library dependency. 
- Then just run the main class Robocode.

- Make sure your robot is in a top level package called 'robots'. The battle editor seems to look for this sepcific package name and it must be the highest package.
- It is possible that the robots do not appear in the Robocode window. This can be corrected by adding the location of the 'robots' directory in 'Development Options' accessible via the 'Options -> Preferences'
- To make your custom robot appear in the battle editor's list, add your 'target' directory as a location in the devlopment options 
Note: Although the above works by just adding the 'robocode.jar' as a dependency, you will need to have all the other jars (robocode core, robocode ui etc) in the same directory as 'robocode.jar'. Robocode depends on all the libs and uses the 'robocode.jar' as a gateway to access the rest of the dependencies.


# Run Configurations
There are 3 ways to run Robocode
    - Robocode run configuration without GUI
        ``  Main class: Robocode
            Args: -nodisplay -battle battles/robot.battle -results battles/battle_results.txt
            VM options: -Ddebug=true -Dsun.io.useCanonCaches=false -DNOSECURITY=true -Xmx4G``
    - Battle engine
        ``VM options: -Ddebug=true -Dsun.io.useCanonCaches=false -DNOSECURITY=true -Xmx4G``
    - Robocode GUI 
        ``  Main class: Robocode
            VM options: -Ddebug=true -Dsun.io.useCanonCaches=false -DNOSECURITY=true -Xmx4G``

Note: -Ddebug=true -Dsun.io.useCanonCaches=false might cause Robocode to hang            
