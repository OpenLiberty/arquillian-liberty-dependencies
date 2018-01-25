# Arquillian Liberty Dependencies

The Liberty dependency bundles for Arquillian make it easy for you to manage versions for your Arquillian installation, the Arquillian Liberty Managed and Remote containers, and JUnit and TestNG libraries. 

## Dependencies

In this repository, you'll find 4 artifacts that you can use in your Maven and Gradle projects. They are:

- `io.openliberty.arquillian:arquillian-liberty-managed-junit`, for using the Arquillian Liberty Managed container with JUnit
- `io.openliberty.arquillian:arquillian-liberty-managed-testng`, for using the Arquillian Liberty Managed container with TestNG
- `io.openliberty.arquillian:arquillian-liberty-remote-junit`, for using the Arquillian Liberty Remote container with JUnit
- `io.openliberty.arquillian:arquillian-liberty-remote-testng`, for using the Arquillian Liberty Remote container with TestNG

## Usage

### Maven

In order to prevent dependency conflicts, you'll need to use the `arquillian-bom` in conjunction with a dependency bundle. While the `arquillian-liberty-dependencies` parent POM already contains the `arquillian-bom`, this is used only to build the dependency bundle artifacts and will not be applied to your project when you include them. The version of the `arquillian-bom` that you specify will apply to transitive dependencies of your project. 

**Example:**

```
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.jboss.arquillian</groupId>
			<artifactId>arquillian-bom</artifactId>
			<version>1.1.13.Final</version>
			<scope>import</scope>
			<type>pom</type>
		</dependency>
	</dependencies>
</dependencyManagement>

<dependencies>
	...
	<dependency>
		<groupId>io.openliberty.arquillian</groupId>
		<artifactId>arquillian-liberty-managed-junit</artifactId>
		<version>1.0.0.Final</version>
		<type>pom</type>
		<scope>test</scope>
	</dependency>
	...
</dependencies>
```

You can use the command `mvn dependency:list` to view transitive dependencies and their versions.

A complete Maven example, used as an integration test for this repository, is available in the `it/managed-tests` folder. 

### Gradle

Unlike in Maven, the `arquillian-bom` is not required in Gradle in order to prevent dependency conflicts. When you add a dependency bundle artifact to your Gradle build, Gradle will resolve artifacts versions for transitive dependencies of the bundle using the `arquillian-bom` specified in the `arquillian-liberty-dependencies` parent POM. As of version `1.0.0.Final`, the `arquillian-bom` version is set to `1.2.0`. 

If you want to override the version of `arquillian-bom` specified in the `arquillian-liberty-dependencies` parent POM, you'll need to use Spring's [`dependency-management-plugin`](https://spring.io/blog/2015/02/23/better-dependency-management-for-gradle) to get Gradle to read and understand BOMs. 

**Using the `dependency-management-plugin`:**

Step 1: Add the plugin as a buildscript dependency:

```
buildscript {
	...
	dependencies {
		...
		classpath "io.spring.gradle:dependency-management-plugin:1.0.4.RELEASE"
		...
	}
	...
}
```

Step 2: Apply the plugin:

```
apply plugin: "io.spring.dependency-management"
```

Step 3: Add a `dependencyManagement` block that contains the `arquillian-bom`:

```
dependencyManagement {
    imports {
        mavenBom "org.jboss.arquillian:arquillian-bom:1.1.13.Final"
    }
}
```

You can use the command `gradle dependencies` to view transitive dependencies and their versions. 

The previous example uses version `1.1.13.Final` of the `arquillian-bom`. A full Gradle example is available in the `gradle-tests` folder. 