package cat.calidos.snowpackage.jsxpackage;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
		() -> assertTrue("Does not start with <slot", slots.startsWith("<slot")),
		() -> assertTrue("Does not terminate correctly", slots.endsWith("/>")),
		() -> assertTrue("Does not contain a fragment", slots.contains("name=\"___fragment\"")),
		() -> assertTrue("Fragment not starting correctly", slots.contains("start=\"164\" ")),
		() -> assertTrue("Fragment not end correctly",slots.contains("end=\"270\"/>"))
	);

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

