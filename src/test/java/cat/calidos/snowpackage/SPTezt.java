package cat.calidos.snowpackage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.api.APITezt;
import cat.calidos.morfeu.webapp.UITezt;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class SPTezt extends UITezt {

protected static String DEFAULT_URL = "http://localhost:4200";
protected static String DEFAULT_WEBAPP_BASE_URL = DEFAULT_URL+"/";
private SPUITezt tezt;

@BeforeEach
public void setup() {

	open(appBaseURL);
	tezt = new SPUITezt();

}


@Test @DisplayName("Foo")
public void testCode() {
	
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

	tezt.setInput(code);
	Optional<String> outputOpt = tezt.run();
	assertTrue(outputOpt.isPresent());
	String slot = outputOpt.get();
	assertAll("",
		() -> assertNotNull(slot),
		() -> assertTrue(slot.startsWith("<slot")),
		() -> assertTrue(slot.endsWith("/>")),
		() -> assertTrue(slot.contains("name=\"___fragment\"")),
		() -> assertTrue(slot.contains("start=\"164\" ")),
		() -> assertTrue(slot.contains("end=\"270\"/>"))
	);
	
}


}

/*
 *	  Copyright 2020 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */


