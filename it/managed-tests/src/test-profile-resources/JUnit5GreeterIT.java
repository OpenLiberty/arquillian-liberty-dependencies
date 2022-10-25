/*
 * Copyright 2022, IBM Corporation, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package application.rest;

import org.arquillian.example.Greeter;
import org.arquillian.example.PhraseBuilder;

import java.io.File;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(ArquillianExtension.class)
public class JUnit5GreeterIT {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class).addClasses(Greeter.class, PhraseBuilder.class);
        System.out.println(archive.toString());
        return archive;
    }

    @Inject
    Greeter greeter;

    @Test
    public void should_create_greeting() {
        String greeting = greeter.createGreeting("Earthling");
        Assertions.assertEquals("Hello, Earthling!", greeting);
        greeter.greet(System.out, "Earthling");
    }
}
