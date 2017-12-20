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
1. Install Lombok, MapStruct nad NodeJS plugins under File -> Settings -> Plugins -> Browse repositories... search for the Lombok, MapStruct support and NodeJS plugins and install them all.
2. Check the Enable annotation processing checkbox under File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors.
3. Download the intellij-java-google-style.xml file from the http://code.google.com/p/google-styleguide/ repo. 
Under File -> Settings -> Editor -> Code Style import the google-styleguide (gear icon -> Import Scheme -> Intellij IDEA code style XML) and choose it as current code style for the project.
4. Enable ESLint under File -> Settings -> Languages and Frameworks -> JavaScript -> Code Quality Tools -> ESLint and set "Node interpreter" to `~/mots/.gradle/nodejs/node-v6.11.4-linux-x64/bin/node` and "ESLint package" to `~/mots/node_modules/eslint`
(you need to run `gradle build` or `./gradlew build` before this to install gradle and npm dependencies)

## Data model changes
1. Generate the migration using the gradle task (`./gradlew dbDiff`), the migration file will be saved at /resources/db/changelog and have the following format yyyyMMdd_HHmm.mysql.sql (e.g. 20171102_1634.mysql.sql)
2. Include the generated migration in the changelog by adding the following line to the liquibase-changelog.xml file: `<include file="{migration file name}" relativeToChangelogFile="true" />`

## Loading initial test data
1. In `mots` root directory run:
`mysql -uroot -p mots < src/main/resources/test_modules.sql`
2. Type your root password

## Add VOTO ivrApiKey
1. Go into ~/.gradle
2. Create gradle.properties file (it will overwrite project's properties)
3. Add `ivrApiKey=<voto-api-key>` to set VOTO communication.

## Load locations locally when starting the server
1. Set `loadLocations=true` in project or home `gradle.properties` file.
2. Run `./gradlew bootRun`.
3. After starting the server locations from xlsx should be added.

## Stopping the Service
To stop the service (when it is running with `gradle bootRun`) use Control-C.