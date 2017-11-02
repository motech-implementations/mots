## Quick Start
1. Clone this repository from GitHub.

 ```shell
 git clone https://github.com/motech-implementations/mots.git
 ```
2. Run `gradle build` to build. After the build steps finish, you should see 'Build Successful'.
3. Start the service with `gradle bootRun`. Once it is running, you should see
'Started Application in NN seconds'. Your console will not return to a prompt as long as
the service is running. The service may write errors and other output to your console.

## Stopping the Service
To stop the service (when it is running with `gradle bootRun`) use Control-C.

## Development
1. Install Lombok plugin under File -> Settings -> Plugins -> Browse repositories... search for the Lombok plugin nad click Install.
2. Check the Enable annotation processing checkbox under File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors.
3. Download the intellij-java-google-style.xml file from the http://code.google.com/p/google-styleguide/ repo. 
Under File -> Settings -> Editor -> Code Style import the google-styleguide (gear icon -> Import Scheme -> Intellij IDEA code style XML) and choose it as current code style for the project.

## Data model changes
1. Generate the migration using the gradle task (./gradlew liquibaseDiffChangelog), the migration file will be saved at /resources/db/changelog and have the following format yyyyMMdd_HHmm.mysql.sql (e.g. 20171102_1634.mysql.sql)
2. Include the generated migration in the changelog by adding the following line to the liquibase-changelog.xml file: <include file="{migration file name}" relativeToChangelogFile="true" />
