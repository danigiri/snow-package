package cat.calidos.snowpackage.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

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
	String codeSlots = DaggerSPCellSlotParserComponent.builder().fromPath("").withCode(code).build().codeSlots();
	File contentFile = new File("./target/classes/test-resources/documents/example-1-edit.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);
	String jsx = DaggerSPCellSlotInjectorComponent.builder().withContent(content).andCode(code).build().code().get();
	//System.out.println(jsx);

	assertAll("check jsx output",
		() -> assertTrue(jsx.startsWith("// comment"), "Did not start with suitable comment"),
		() -> assertTrue(jsx.contains("const slot1 ="), "Does not declare slot1 variable"),
		() -> assertTrue(jsx.contains("<Data number=\"32\"/>"), "Incorrect data node"),
		() -> assertFalse(jsx.contains("number=\"42\""), "Incorrect data2 nodes"),
		() -> assertFalse(jsx.contains("text=\"blahblah\""), "Incorrect data2 nodes"),
		() -> assertTrue(jsx.contains("<Data2 number=\"32\" text=\"blahbla4\"/>"), "Incorrect data2 nodes"),
		() -> assertTrue(jsx.endsWith("ReactDOM.render(slot1, document.getElementById('root'));\n"), "Incorrect ending")
	);

}

@Test @DisplayName("Test inject JSX codeslots with precision")
public void testInjectJSXCodeSlotsPrecision() throws Exception {

	String code = read("./target/classes/test-resources/documents/example-3.jsx");
	String codeSlots = DaggerSPCellSlotParserComponent.builder().fromPath("").withCode(code).build().codeSlots();
	String content = read("./target/classes/test-resources/documents/example-3.xml"); //unmodified
	String jsx = DaggerSPCellSlotInjectorComponent.builder().withContent(content).andCode(code).build().code().get();
	//System.out.println(jsx);

}


@Test @DisplayName("Test inject minimal JSX code 1")
public void testGenerateCodeMinimal1() throws Exception {

	String code = read("./target/classes/test-resources/documents/minimal-1.jsx");
	String codeSlots = DaggerSPCellSlotParserComponent.builder().fromPath("").withCode(code).build().codeSlots();
	String content = read("./target/classes/test-resources/documents/minimal-1-edit.xml");
	String jsx = DaggerSPCellSlotInjectorComponent.builder().withContent(content).andCode(code).build().code().get();
	//System.out.println(jsx);
	assertEquals("let a=<p/><p/>;", jsx);

}


@Test @DisplayName("Test inject minimal JSX code 2")
public void testGenerateCodeMinimal2() throws Exception {

	String code = read("./target/classes/test-resources/documents/minimal-2.jsx");
	String codeSlots = DaggerSPCellSlotParserComponent.builder().fromPath("").withCode(code).build().codeSlots();
	String content = read("./target/classes/test-resources/documents/minimal-2-edit.xml");
	String jsx = DaggerSPCellSlotInjectorComponent.builder().withContent(content).andCode(code).build().code().get();
	//System.out.println(jsx);
	assertEquals("let a=<><p/><p/></>;", jsx);

}


@Test @DisplayName("Test inject minimal JSX code 2")
public void testGenerateCodeMinimal3() throws Exception {

	String code = read("./target/classes/test-resources/documents/minimal-3.jsx");
	String codeSlots = DaggerSPCellSlotParserComponent.builder().fromPath("").withCode(code).build().codeSlots();
	String content = read("./target/classes/test-resources/documents/minimal-3-edit.xml");
	String jsx = DaggerSPCellSlotInjectorComponent.builder().withContent(content).andCode(code).build().code().get();
	//System.out.println(jsx);
	assertEquals("let a=<><div>\n\t\t<p/>\n\t\t<p/>\n\t</div></>;", jsx);

}


private String read(String path) throws IOException {
	return FileUtils.readFileToString(new File(path), Config.DEFAULT_CHARSET);
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

