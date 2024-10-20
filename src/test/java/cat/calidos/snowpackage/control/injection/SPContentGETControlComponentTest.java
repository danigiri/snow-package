package cat.calidos.snowpackage.control.injection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.snowpackage.SPTezt;
import cat.calidos.snowpackage.model.injection.SPCellSlotParserComponent;


public class SPContentGETControlComponentTest extends SPTezt {


@Test @DisplayName("Generate JSX content")
public void testGenerateJSXContent() throws Exception {

	String content = DaggerSPContentGETControlComponent
			.builder()
			.fromPath("classes/test-resources/documents/example-1.jsx")
			.withPrefix("file:./target/")
			.filters(SPCellSlotParserComponent.DEFAULT_LOAD_FILTER)
			.andProblem("")
			.build()
			.parsedCode();
	//System.out.println(content);
	assertNotNull(content);
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
