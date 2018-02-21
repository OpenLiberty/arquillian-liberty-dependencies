package application.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class TNGTransactionRestEndpointIT extends Arquillian {

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class).addPackage("application").addPackage("application.rest")
				.addAsLibraries(Maven.resolver().resolve("org.testng:testng:6.13.1").withTransitivity().asFile());
	}

	@Test
	public void testRunningOnServer() throws Exception {
		Reporter.log("--- test running on server", true);
		Properties p = System.getProperties();
		Enumeration<Object> keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) p.get(key);
			if (key.equals("wlp.process.type") && value.equals("server")) {
				return;
			}
		}
		Assert.assertTrue(false);
	}

	@Test
	public void testDataExistsAtEndpoint() throws Exception {
		System.out.println("--- test data exists at endpoint");
		URL endpoint = new URL("http://localhost:9080/myLibertyApp/api/transactions");
		System.out.println("endpoint url: " + endpoint);
		String body = readAllAndClose(endpoint.openStream());
		int bodyLength = body.length();
		System.out.println("body length is " + bodyLength);
		Assert.assertTrue(bodyLength > 0);
	}

	@Test
	public void testNewDataAddedToEndpoint() throws Exception {
		System.out.println("--- test new data added to endpoint");
		String firstBody = readAllAndClose(new URL("http://localhost:9080/myLibertyApp/api/transactions").openStream());
		int firstBodyLength = firstBody.length();
		Thread.sleep(12000);
		String secondBody = readAllAndClose(
				new URL("http://localhost:9080/myLibertyApp/api/transactions").openStream());
		int secondBodyLength = secondBody.length();
		Assert.assertTrue(secondBodyLength > firstBodyLength);
	}

	String readAllAndClose(InputStream is) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int read;
			while ((read = is.read()) != -1) {
				out.write(read);
			}
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
		return out.toString();
	}

}
