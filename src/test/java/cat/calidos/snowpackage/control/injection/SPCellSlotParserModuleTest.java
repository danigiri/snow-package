package cat.calidos.snowpackage.control.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.snowpackage.SPTezt;

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

	BiFunction<List<String>, Map<String, String>, String> f = SPCellSlotParserModule.postCode(task);
	ArrayList<String> pathElems = new ArrayList<String>(1);
	pathElems.add("jsx");
	Map<String, String> params = MorfeuUtils.paramStringMap(SPCellSlotParserModule.CODE_PARAM, code);

	String expected = "[{\"type\":\"___fragment\", \"start\":\"164\", \"end\":\"270\"}";
	String slots = f.apply(pathElems, params);
	assertAll("test slot",
		() -> assertNotNull(slots),
		() -> assertTrue(slots.startsWith(expected), "Wrong slot, got '"+slots+"' instead")
	);

}


public ReadyTask jsxTask() {

	Properties properties = new Properties();
	properties.putAll(MorfeuUtils.paramStringMap(SPCellSlotParserModule.NODEFOLDER_PROPERTY, nodeFolder,
													SPCellSlotParserModule.TSNODE_PROPERTY, tsNodeCommand,
													SPCellSlotParserModule.TSCODE_PROPERTY, tsCode));
	return SPCellSlotParserModule.runJSX(properties);

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

