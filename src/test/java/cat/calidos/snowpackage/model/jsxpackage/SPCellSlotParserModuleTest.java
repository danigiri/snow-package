package cat.calidos.snowpackage.model.jsxpackage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.snowpackage.SPTezt;
import cat.calidos.snowpackage.model.injection.SPCellSlotParserModule;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPCellSlotParserModuleTest extends SPTezt {


@Test @DisplayName("Test generate JSX")
public void testRunJSX() {

	Properties properties = new Properties();
	properties.putAll(MorfeuUtils.paramStringMap(SPCellSlotParserModule.NODEFOLDER_PROPERTY, "aa",
													SPCellSlotParserModule.TSNODE_PROPERTY, "/bb",
													SPCellSlotParserModule.TSCODE_PROPERTY, "cc.ts"));

	ReadyTask task = SPCellSlotParserModule.runJSX(properties);
	assertAll("basic checks",
		() -> assertNotNull(task),
		() -> assertTrue(task.toString().contains("aa /bb cc.ts --jsx"), "Wrong task "+task.toString()));

}


@Test @DisplayName("Test get slots")
public void testApply() {

	ReadyTask task = jsxTask();

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
	//System.err.println(code);

	String expected = "[{\"type\":\"___fragment\", \"start\":\"166\", \"end\":\"267\"}";
	String slots = SPCellSlotParserModule.slots(task, code);
	assertAll("test slot",
		() -> assertNotNull(slots),
		() -> assertTrue(slots.startsWith(expected), "Wrong slot, got '"+slots+"' instead")
	);

}



@Test @DisplayName("Test start and end of code cells")
public void testStartEnd() throws Exception {

	ReadyTask task = jsxTask();

	String code = readCode("./target/classes/test-resources/documents/example-3.jsx");
	String slots = SPCellSlotParserModule.slots(task, code);
	//System.err.println(slots);
	//[{"type":"___code", "start":"9", "end":"46"},
	//{"type":"___code", "start":"61", "end":"98"}]
	assertAll("test slot",
			() -> assertNotNull(slots),
			() -> assertTrue(slots.contains("{\"type\":\"___code\", \"start\":\"9\", \"end\":\"46\"}")),
			() -> assertTrue(slots.contains("{\"type\":\"___code\", \"start\":\"61\", \"end\":\"98\"}"))
		);
}


@Test @DisplayName("Minimal test 1")
public void testStartEndMinimal1() throws Exception {

	ReadyTask task = jsxTask();

	String code = readCode("./target/classes/test-resources/documents/minimal-1.jsx");
	String slots = SPCellSlotParserModule.slots(task, code);
	assertEquals("[{\"type\":\"___code\", \"start\":\"6\", \"end\":\"10\"}]\n", slots);

	//	let a=<p/>;
	//	01234567890
	//	      *   * 
}


@Test @DisplayName("Minimal test 21")
public void testStartEndMinimal2() throws Exception {

	ReadyTask task = jsxTask();

	String code = readCode("./target/classes/test-resources/documents/minimal-2.jsx");
	String slots = SPCellSlotParserModule.slots(task, code);
	assertEquals("[{\"type\":\"___fragment\", \"start\":\"8\", \"end\":\"12\"}]\n", slots);	// we skip <> and </>

	//	let a=<><</>p/>;
	//	0123456789012345
	//	      *   * 

}


public ReadyTask jsxTask() {

	Properties properties = new Properties();
	properties.putAll(MorfeuUtils.paramStringMap(SPCellSlotParserModule.NODEFOLDER_PROPERTY, nodeFolder,
													SPCellSlotParserModule.TSNODE_PROPERTY, tsNodeCommand,
													SPCellSlotParserModule.TSCODE_PROPERTY, tsCode));
	return SPCellSlotParserModule.runJSX(properties);

}


private String readCode(String path) throws IOException {
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

