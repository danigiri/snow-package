package cat.calidos.snowpackage.control.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.snowpackage.SPTezt;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPFileContentControlModuleTest extends SPTezt {

private String jsxPath;
private List<String> pathElems;


@BeforeEach
public void setup() {

	jsxPath = "classes/test-resources/documents/example-1.jsx";
	pathElems = new ArrayList<String>(0);
	pathElems.add("/content/"+jsxPath);
	pathElems.add(jsxPath);


}


@Test @DisplayName("Generate JSX content")
public void testGenerateJSXContent() throws Exception {

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:./target/");

	String content = SPFileContentControlModule.get(config).apply(pathElems, MorfeuUtils.emptyParamStringMap());
	//System.err.println(content);
	assertNotNull(content);
	compareWithXMLFile(content, "./target/classes/test-resources/documents/example-1.xml");

}


@Test @DisplayName("Save JSX content")
public void testSaveJSX() throws Exception {
	
	File contentFile = new File("./target/classes/test-resources/documents/example-1-edit.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);

	String tmp = setupTempDirectory().getAbsolutePath()+"/";
	System.err.println("SPFileContentControlModuleTest::Using '"+tmp+"' as temporary test folder");

	// we copy the original JSX source to the temp folder so new content can be injected and modified
	File originalJSXFile = new File("./target/"+jsxPath);
	File destinationJSXFile = new File(tmp+jsxPath);
	FileUtils.copyFile(originalJSXFile, destinationJSXFile);

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:"+tmp);

	Map<String, String> oarams = MorfeuUtils.paramStringMap(GenericHttpServlet.POST_VALUE, content);
	String result = SPFileContentControlModule.post(config).apply(pathElems, oarams);
	//System.err.println(result);

	// check operation result first
	assertAll("check result",
		() -> assertNotNull(result),
		() -> assertFalse(result.contains("KO")),
		() -> assertFalse(result.contains("problem:")),
		() -> assertTrue(result.contains("OK"))		
	);
	
	// now the injected code
	String jsx = FileUtils.readFileToString(destinationJSXFile, Config.DEFAULT_CHARSET);
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

