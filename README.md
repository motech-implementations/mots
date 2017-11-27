## Quick Start backend development
Clone this repository from GitHub:
 ```shell
 git clone https://github.com/motech-implementations/mots.git
 ```
1. Start the service with `./gradlew bootRun` or `gradle bootRun`. On first run it installs all 
dependencies like NPM, node modules etc. Once it is running, you should see 'Started Application in NN seconds'. 
Your console will not return to a prompt as long as the service is running. 
The service may write errors and other output to your console.
2. Best way for speed development is to add a `bootRun` Gradle configuration to IntelliJ Idea, and
run it with `Debug` option.
3. Hot-swapping Java classes without rerunning `./gradlew bootRun`: try clicking CTRL+F9 to build project (ie. all Java classes in classpath),
and accept a prompt asking about reloading changed classes.

## Frontend Hot-Reload with Webpack watch
1. Start Java server with `gradle bootRun` or `./gradlew bootRun`.
2. Run `gradle webpackWatch` or `./gradlew webpackWatch` in separate terminal (or ideally in IntelliJ Idea)
3. Profit - your .scss, and .js files are reloaded automatically. Refresh your browser.

## IntelliJ Idea development
1. Install Lombok and MapStruct plugins under File -> Settings -> Plugins -> Browse repositories... search for the Lombok and MapStruct support plugins and install them both.
2. Check the Enable annotation processing checkbox under File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors.
3. Download the intellij-java-google-style.xml file from the http://code.google.com/p/google-styleguide/ repo. 
Under File -> Settings -> Editor -> Code Style import the google-styleguide (gear icon -> Import Scheme -> Intellij IDEA code style XML) and choose it as current code style for the project.

## Data model changes
1. Generate the migration using the gradle task (`./gradlew dbDiff`), the migration file will be saved at /resources/db/changelog and have the following format yyyyMMdd_HHmm.mysql.sql (e.g. 20171102_1634.mysql.sql)
2. Include the generated migration in the changelog by adding the following line to the liquibase-changelog.xml file: `<include file="{migration file name}" relativeToChangelogFile="true" />`

## Stopping the Service
To stop the service (when it is running with `gradle bootRun`) use Control-C.