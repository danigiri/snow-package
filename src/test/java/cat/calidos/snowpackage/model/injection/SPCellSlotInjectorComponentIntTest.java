package cat.calidos.snowpackage.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Tezt;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPCellSlotInjectorComponentIntTest extends Tezt {


@Test @DisplayName("Test inject JSX codeslots")
public void testInjectJSXCodeSlots() throws Exception {

	File codeFile = new File("./target/classes/test-resources/documents/example-1.jsx");
	String code = FileUtils.readFileToString(codeFile, Config.DEFAULT_CHARSET);
	File contentFile = new File("./target/classes/test-resources/documents/example-1.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);
	String jsx = DaggerSPCellSlotInjectorComponent.builder().withContent(content).andCode(code).build().code().get();
	System.out.println(jsx);

	assertAll("check jsx output",
		() -> assertTrue(jsx.startsWith("// comment"), "Did not start with suitable comment"),
		() -> assertTrue(jsx.contains("const slot1 ="), "Does not declare slot1 variable"),
		() -> assertTrue(jsx.contains("<data number=\"32\"/>"), "Incorrect data node"),
		() -> assertFalse(jsx.contains("number=\"42\""), "Incorrect data2 nodes"),
		() -> assertFalse(jsx.contains("text=\"blahblah\""), "Incorrect data2 nodes"),
		() -> assertTrue(jsx.contains("<data2 number=\"32\" text=\"blahbla4\"/>"), "Incorrect data2 nodes"),
		() -> assertTrue(jsx.endsWith("ReactDOM.render(slot1, document.getElementById('root'));\n"), "Incorrect ending")
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

