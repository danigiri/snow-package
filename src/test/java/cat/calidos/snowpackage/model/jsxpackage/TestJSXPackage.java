package cat.calidos.snowpackage.model.jsxpackage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.snowpackage.SPTezt;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TestJSXPackage extends SPTezt {


@Test @DisplayName("Test basic JSX structure")
public void testBasicCode() throws Exception {

	String code = "function formatName(user) {\n" + 
			"  return user.firstName + ' ' + user.lastName;\n" + 
			"}\n" + 
			"\n" + 
			"const user = {\n" + 
			"  firstName: 'Harper',\n" + 
			"  lastName: 'Perez',\n" + 
			"};\n" + 
			"\n" + 
			"const element = \n" + 
			"      <>\n" + 
			"         <h1>Hello, {formatName(user)}!</h1>\n" + 
			"          <span>how are <b>you</b> doing?</span>\n" + 
			"      </>;\n" + 
			"\n" + 
			"ReactDOM.render(element, document.getElementById('root'));";

	String slots = runCode(code).trim();
	assertAll("Checking basic code slots",
		() -> assertNotNull(slots),
		() -> assertTrue(slots.startsWith("<slot"), "Does not start with <slot"),
		() -> assertTrue(slots.endsWith("/>"), "Does not terminate correctly"),
		() -> assertTrue(slots.contains("name=\"___fragment\""), "Does not contain a fragment"),
		() -> assertTrue(slots.contains("start=\"164\" "), "Fragment not starting correctly"),
		() -> assertTrue(slots.contains("end=\"270\"/>"), "Fragment not end correctly")
	);

}


@Test @DisplayName("Test multiple JSX structures")
public void testMultipleStructures() throws Exception {

	String code = "function formatName(user) {\n" + 
			"  return user.firstName + ' ' + user.lastName;\n" + 
			"}\n" + 
			"\n" + 
			"const user = {\n" + 
			"  firstName: 'Harper',\n" + 
			"  lastName: 'Perez',\n" + 
			"};\n" + 
			"\n" + 
			"const element = \n" + 
			"      <>\n" + 
			"         <h1>Hello, {formatName(user)}!</h1>\n" + 
			"          <span>How are <b>you</b> doing?</span>\n" + 
			"      </>;\n" + 
			"const element2 = \n" + 
			"      <>\n" + 
			"         <h1>Hello, again {formatName(user)}!</h1>\n" + 
			"          <span>How are <b>you</b> really doing?</span>\n" + 
			"      </>;\n" + 
			"\n" + 
			"ReactDOM.render(element, document.getElementById('root'));" +
			"ReactDOM.render(element2, document.getElementById('root'));";

	String slots = runCode(code).trim();
	System.err.println(slots);
	assertAll("Checking basic code slots",
		() -> assertNotNull(slots),
		() -> assertNotNull(slots),
		() -> assertTrue(slots.startsWith("<slot"), "Does not start with <slot"),
		() -> assertTrue(slots.endsWith("/>"), "Does not terminate correctly"),
		() -> assertTrue(slots.contains("name=\"___fragment\""), "Does not contain a fragment"),
		() -> assertTrue(slots.contains("start=\"164\" "), "Fragment 1 not starting correctly"),
		() -> assertTrue(slots.contains("end=\"270\"/>"), "Fragment 1 not end correctly"),
		() -> assertTrue(slots.contains("start=\"296\" "), "Fragment 2 not starting correctly"),
		() -> assertTrue(slots.contains("end=\"415\"/>"), "Fragment not end correctly")
	);

}



@Test @DisplayName("Test incorrect code")
public void testIncorrectCode() throws Exception {

	String code = "const element = <b>";

	String slots = runCode(code);
	assertTrue(slots.isEmpty(), "Non empty output");
}

}


/*
 *    Copyright 2020 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

