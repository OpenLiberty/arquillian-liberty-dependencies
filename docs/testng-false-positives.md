### False positive test results in certain versions of TestNG with Arquillian

When using certain versions of Arquillian and TestNG, it is possible to experience an issue where tests results display as passing without actually being executed. This has been verified for the following version combinations of Arquillian and TestNG, but may be present in other version combinations as well.

- TestNG `6.13.1` and Arquillian `1.1.15.Final`
- TestNG `6.13.1` and Arquillian `1.2.0.Final`

More information about this issue can be found [here](https://github.com/cbeust/testng/issues/1513).

#### Identification

This problem can be easily identified by a `java.lang.NoClassDefFoundError on a TestNG class when running the tests. For example:

```
[ERROR   ] SRVE0777E: Exception thrown by application class 'java.lang.ClassLoader.defineClass1:-2'
java.lang.NoClassDefFoundError: org/testng/IHookable
	at java.lang.ClassLoader.defineClass1(Native Method)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:763)
	at com.ibm.ws.classloading.internal.AppClassLoader.definePackageAndClass(AppClassLoader.java:335)
	at [internal classes]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at org.jboss.arquillian.protocol.servlet.runner.ServletTestRunner.executeTest(ServletTestRunner.java:137)
	at org.jboss.arquillian.protocol.servlet.runner.ServletTestRunner.execute(ServletTestRunner.java:117)
	at org.jboss.arquillian.protocol.servlet.runner.ServletTestRunner.doGet(ServletTestRunner.java:86)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:687)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
	at com.ibm.ws.webcontainer.servlet.ServletWrapper.service(ServletWrapper.java:1290)
	at [internal classes]
```

However, all tests will be shown as passing, despite not being executed.

#### Resolution

As indicated by the error message shown above, the problem is that the TestNG JAR is not being added to the test container. The solution is to manually add the TestNG JAR to your archive when creating the deployment.

For example:

```
@Deployment
public static WebArchive createDeployment() {
    final PomEquippedResolveStage pomEquippedResolveStage = Maven.resolver().loadPomFromFile("pom.xml");
    File[] mavenFiles = pomEquippedResolveStage.importRuntimeDependencies().resolve().withTransitivity().asFile();
	return ShrinkWrap.create(WebArchive.class).addPackage("mypackage")
		.addAsLibraries(mavenFiles)
		.addAsLibraries(pomEquippedResolveStage.resolve("org.testng:testng:6.13.1").withTransitivity().asFile());
}
```