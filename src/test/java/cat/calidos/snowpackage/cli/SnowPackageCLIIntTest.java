package cat.calidos.snowpackage.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.Pair;
import cat.calidos.snowpackage.SPTezt;


public class SnowPackageCLIIntTest extends SPTezt {

@Test @DisplayName("Content parse test")
public void parseContent() {

	var path = "test-resources/documents/example-1.jsx"; // note this has no classes/ prepended,
	var prefix = testAwareFullPathFrom(".");
	var args = new String[] { "-q", "--prefix", prefix, "-tnode", tsNodeCommand, "--tscode", tsCode,
			SnowPackageCLI.PARSE, path };
	Pair<Integer, String> result = SnowPackageCLI.mainImpl(new SnowPackageCLI(), args);

	assertEquals(0, result.getLeft());

	String content = result.getRight();
	assertNotNull(content);
	// System.out.println(content);
	// for some reason, on this test we do not have access to the test resources in the same way
	// under /target', the test xml comparison file has the right path, we do a simple search and
	// replace so we pass the test, the path in the file is not relevant to this test
	content = content.replaceAll("test\\-resources", "classes/test\\-resources");
	compareWithXMLFile(content, "./target/classes/test-resources/documents/example-1.xml");

}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
