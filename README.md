# JAVA (JVM) Toolchains
## A short introduction to JVM toolchains

Using Eclipse or IntelliJ project is easy, but to build that project, you'll need the IDE.  
And, for example CI (Continous Integration) it is not a good idea.
Also, IDE projects does not support maven library management well.

## Maven dependencies
In software engineering, it is very common to use dependencies other than the standard library.  

There are libraries for many things, like [gson](https://github.com/google/gson) Google JSON serialization/deserialization library.  

**Maven** helps you with this, by __automatically__ resolving, downloading required dependencies when you want to compile your project.

> To better understand this theme, I advice setting up the same environment on your computer.  

> If you stuck, check TODO LINK [solutions](...) branch.

### Maven repository (repo for short)
The time, I'm writing it, the biggest maven repository is [Maven Central](https://search.maven.org/) with millions of artifacts.  
But its size makes it harder to understand.  

Let's see a much smaller maven repository, my personal maven server: [maven.kosmx.dev](https://maven.kosmx.dev/)  
***
**What is a maven server anyway**  
The maven server is just a simple, static website, hosting `.jar` files.  
 
Why these many folders in folders?!  

Because there are so many java artifacts (libraries), all of those has to be unique. (or the program won't know which of those is what you need)

Every artifact has 3 property what will help find it:  
`group id`, `artifact id`, `version`

***
**Naming conventions**  
Every artifact has a `group id`. This should start with the author's (or organization's) domain in reverse order:  
e.g. I'm `kosmx` on GitHub, so [`kosmx.github.io`](https://kosmx.github.io/) is mine. So I can start my library groups with `io.github.kosmx`.  

But I own [`kosmx.dev`](https://kosmx.dev/) domain too, so I can use `dev.kosmx`.  


> and, if I'm doing this library as a part of a project, I can continue adding to the group-id.  
`io.github.kosmx.edu.math`


Let's look into [maven.kosmx.dev/io/github/kosmx/edu/math/vector3d/](https://maven.kosmx.dev/io/github/kosmx/edu/math/vector3d/)  
This is a simple 3-dimensional mathematical vector implementation.  

Here, all the folders, except the last are the group-id (just instead of dots, using slash):  
group id: `io.github.kosmx.edu.math`, artifact id: `vector3d`.

Let's look into [maven-metadata.xml](https://maven.kosmx.dev/io/github/kosmx/edu/math/vector3d/maven-metadata.xml)
```xml
<metadata>
    <groupId>io.github.kosmx.edu.math</groupId>
    <artifactId>vector3d</artifactId>
    <versioning>
        <latest>1.0.0</latest>
        <release>1.0.0</release>
        <versions>
            <version>1.0.0</version>
        </versions>
        <lastUpdated>20210515095830</lastUpdated>
    </versioning>
</metadata>
```
This metadata help tools to find artifacts, but most tools does not break if maven-netadata.xml is missing.  
Here we can see the groupId and artifactId, also the latest release and the list of all the versions available.  
> Some maven servers has a human-friendly interface, where you can see versions without opening this file.

**Now, that we know, there is a `1.0.0` version, we can finally download the library**  
look into [1.0.0/](https://maven.kosmx.dev/io/github/kosmx/edu/math/vector3d/1.0.0/), we see multiple files.  

`*.md5`, `*.shaXXX` are storing checksums, those are optional. 

`vector3d-1.0.0.pom` contains the information about this version and the dependencies this library has.  

`vector3d-1.0.0.jar` is the compiled library. (`$groupId-$versionId.jar`)
> Most libraries does not have a main function, this is not a standalone executable.

`vector3d-1.0.0-sources.jar` (optional) is the `zip` containing the `.java` sources, so developers can look inside the library.  

`vector3d-1.0.0-javadoc.jar` (optional) is the `zip` containing HTML javadoc for this library

## [Maven toolchain](https://maven.apache.org/index.html)
> Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.

> https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html maven in 5 minutes on maven.apache.org

Now, that we learned about these `maven repos`, lets create a project and use a dependency from some repo!

You'll recognise a maven project if you find a `pom.xml` in the project root.  
https://github.com/KosmX/java-toolchains/blob/main/maven-project/pom.xml  

### What is a `pom.xml`?

Let's open the pom.xml in the example!  
(This has been generated usin IntelliJ generator)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>maven-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```
This xml describes the project model: what is the id of our project, dependencies, java version...  

`groupId`, `artifactId`, `version` tags contain the earlier specified properties.  

`properties` contain information about build toolchains, in the example, we set the JVM target to Java 17 LTS, and set the source encoding to UTF-8.  

`dependencies` will describe the used dependencies, `repositories` will describe custom maven servers.  

### Basic maven commands
`mvn clean` clear build cache, artifacts  

`mvn compile` compile `.java` classes into `.class` files  

`mvn package` create a `.jar` file from your project  
> Of course, package will trigger compile, so using this is enough.

`mvn deploy` upload the `.jar` to target repositories (or local repository)  


### Adding dependencies
> To better understand these, I advice following the tutorial.

Our `pom.xml` doesn't have a `depencencies` tag, we should add that.
```xml
    <dependencies>
        
    </dependencies>
```

Now, search an artifact, we want to add: use [gson 2.10 from maven central](https://search.maven.org/artifact/com.google.code.gson/gson/2.10/jar).  

To add a dependency, create a `dependency` tag inside `dependencies` and inside that, set `groupId`, `artifactId` and `version`  
```xml
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10</version>
        </dependency>
```
If you check maven central page, it offers you this exact code snippet.

> Now refresh your IDE project and hopefully gson will appear in external dependencies.

Quickly test if gson is really working:
```java
JsonObject obj = JsonParser.parseString("{\"str\": \"Hello gson!\"}").getAsJsonObject();
System.out.println(obj.get("str").getAsString());
```

### Adding repositories
> Maven central is the largest repo, but not every artifact is uploaded there.  

When a tool looks for an aritfact, it will start checking that in every repo one-by-one until it finds it.  

To add a repository, simply add `resositories` tag and.
And inside that, add a `repository` tag
```xml
        <repository>
            <id>kosmx</id>
            <name>KosmX maven</name>
            <url>https://maven.kosmx.dev/</url>
        </repository>
```
Every repository needs an ID (this id exists in your project, you can use any string describing that repo)  
Name is optional.  

Now, let's test if this really works,  
Add the `vector3d` package from earlier!  

> group id: `io.github.kosmx.edu.math`  
> artifact id: `vector3d`  
> version: `1.0.0`  

> If you don't know what to write, you can find the solution in `solutions` branch!

Here is some test code
```java
Vector3D a = new Vector3D(4, 0, 0);
Vector3D b = new Vector3D(0, 5, 0);
System.out.println(a.cross(b).length());
```

### I want to learn more
> There are way more, maven can do, you can add plugins to modify build logic, add sub-projects, set-up automatic publishing...  

You'll find a detailed documentation about maven on https://maven.apache.org/index.html

## [Gradle](https://gradle.org/) (with Groovy DSL)
Gradle is a modern, highly customizable, fast build tool.  
You can write gradle build-scripts using [Groovy](https://groovy-lang.org/) or [Kotlin](https://kotlinlang.org/)  

> In this tutorial, we'll use Groovy DSL as it is easier to understand without Kotlin knowledge.


You'll recognise a gradle project, if the sources root contains `settings.gradle` and `build.gradle`
> or `settings.gradle.kts` and `build.gradle.kts` if gradle is using Kotlin buildscript

### Gradle usage

It is a common practice to include `gradlew` gradle wrapper in the repository.  
The wrapper is a small script what will automatically download gradle if you want to use it.  
`gradle ...` command will use gradle in PATH (if you installed it earlier)  
`./gradlew ...` will use gradle wrapper  

***

**gradle daemon** gradle uses a daemon to speed-up sequential builds.  
After you first run gradle, a gradle app will continue running in the background.  
If you don't want daemons, use `--no-daemon` flag to disable this behavior.

***

`gradle` : If you run gradle without task, it will configure the project (download dependencies, etc.) but not build anything

`gradle clean` : clean build cache  

`gradle classes` : compile classes  

`gradle build` : run build task, will output your .jar in `./build/libs/`

`gradle --refresh-dependencies` : force re-download all dependencies  

`gradle -stop` : stop every gradle daemons (only stops daemons with the same version)


### Few words about groovy

In groovy, function call brackets can be omitted if there is no nested function.
> `println("test")` and `println "test"` are equivalent.  


Both quotation mark `"` and apostrophe `'` can be used for strings.  
The difference is, you can substitude only if you use quotation mark.  (yes, there is string substitution)
```groovy
String stuff = "world!"
println 'Hello $stuff' // Hello $stuff
println "Hello $stuff" // Hello world!
```

### Gradle template
Unlike maven, you configure gradle using a program, called buildscript.  

This buildscript allows you to make custom steps in build (like replace version string in META-INF or rename assets).  

(This template is generated usin IntelliJ)  
`settings.gradle`
```groovy
rootProject.name = 'gradle-project'
```
This is the project name. Inside settings.gradle, you can declare plugin dependencies.  

`build.gradle`
```groovy
plugins {
    id 'java'
}

group 'org.example'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.0'
}

test {
    useJUnitPlatform()
}
```
gradle is not a java specific tool, it can be used for other languages.  

`plugin`: which plugins do you want to enable. `java-library`, `java` for java code, `maven-publish` to publish to maven server.
> There are many custom plugins like `fabric-loom` which helps building Minecraft mods, but these are not built-in plugins, you'll need to set a repository in `settings.gradle`...

`group` and `version`: the project group and version ID

`repositories`: declare external repositories, `mavenCentral()` will add maven central

`dependencies`: you can declare dependencies here. More about it later.

`test`: configure test task using JUnit

***
### Dependencies
Dependencies can be declared in `dependencies` scope.

dependency can be declared using  
```groovy
implementation("$groupId:$artifactId:$version") // or
implementation(group: "$groupId", name: "$artifactId", version: "$version")
```

There are multiple dependency configurations built-in, but you can add more.  

`implementation`: Dependency is needed both run-time and compile-time. Most of the time you'll use this.  

`runtimeOnly`: Dependency is not needed to compile the project, but needed to run.  

`compileOnly`: Dependency is needed when compiling, but not needed when running.  

`testImplementation`: same as implementation, but for testing only.  
> and the other configurations has test only versions

*** 
**Let's add gson again**  
Just add the following line into `dependencies`  
`implementation 'com.google.code.gson:gson:2.10'`

> Refresh your IDE and you'll see gson in dependencies.
```java
JsonObject obj = JsonParser.parseString("{\"str\": \"Hello gson!\"}").getAsJsonObject();
System.out.println(obj.get("str").getAsString());
```
*** 
### Repositories
Let's see custom maven repos

You add repositories in `repositories` scope.  

`mavenCentral()` will add maven central (no url needed)  
`mavenLocal()` will add local repository (located in `$(HOME)/.m2/repository/`)  

`maven { url = "$url" }` will add a repo by URL  

```groovy
    maven {
        name = "KosmX" // name is optional
        url = "https://maven.kosmx.dev/"
    }
```
> You can define repo name, this will appear in logs if repo is unavailable or something wrong with it. 

***

Now test it, add the earlier seen `vector3d` library!
> group id: `io.github.kosmx.edu.math`  
> artifact id: `vector3d`  
> version: `1.0.0`  

> The solution is in `solutions` branch, check it only if you stuck!  

Now, test it!
```java
Vector3D a = new Vector3D(4, 0, 0);
Vector3D b = new Vector3D(0, 5, 0);
System.out.println(a.cross(b).length());
```

### More gradle
There are pretty good guides in https://gradle.org/

> https://github.com/johnrengelman/shadow one of the most useful gradle plugins.  

> If you want to use Kotlin buildscript, first learn kotlin https://kotlinlang.org/docs/getting-started.html  

> https://github.com/KosmX/fabric-example-mod-kotlin This is an example Minecraft mod using some advanced gradle features and Kotlin. You can check it out.


## explainer
***
I created this because the university JAVA course does not teach about this *very important* thing.