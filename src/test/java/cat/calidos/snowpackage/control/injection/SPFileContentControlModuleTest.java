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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.DaggerXMLParserComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.snowpackage.SPTezt;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPFileContentControlModuleTest extends SPTezt {

private String jsxPath;
private List<String> pathElems;
private String tmp;


@BeforeEach
public void setup() {

	try {
		tmp = setupTempDirectory().getAbsolutePath()+"/";
		System.err.println("SPFileContentControlModuleTest::Using '"+tmp+"' as temporary test folder");
	} catch (Exception e) {}

}


@Test @DisplayName("Generate JSX content")
public void testGenerateJSXContent() throws Exception {

	setupPathElements("classes/test-resources/documents/example-1.jsx");

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:./target/");

	String content = SPFileContentControlModule.get(config).apply(pathElems, MorfeuUtils.emptyParamStringMap());
	//System.err.println(content);
	assertNotNull(content);
	compareWithXMLFile(content, "./target/classes/test-resources/documents/example-1.xml");

}


@Test @DisplayName("Generate JSX content multiple slots")
public void testGenerateJSXContentMultipleSlots() throws Exception {

	setupPathElements("classes/test-resources/documents/example-2.jsx");

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:./target/");

	String content = SPFileContentControlModule.get(config).apply(pathElems, MorfeuUtils.emptyParamStringMap());
	//System.err.println(content);
	assertNotNull(content);

	Document doc = DaggerXMLParserComponent.builder().withContent(content).build().document().get();
	NodeList children = doc.getChildNodes().item(0).getChildNodes();
	assertAll("checking cell slots",
		() -> assertNotNull(children),
		() -> assertEquals(4, children.getLength()),
		() -> assertEquals("codeslot", children.item(1).getNodeName()),
		() -> assertEquals("codeslot", children.item(2).getNodeName())
	);


}



@Test @DisplayName("Save JSX content")
public void testSaveJSX() throws Exception {

	setupPathElements("classes/test-resources/documents/example-1.jsx");

	File contentFile = new File("./target/classes/test-resources/documents/example-1-edit.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);

	// we copy the original JSX source to the temp folder so new content can be injected and modified
	File originalJSXFile = new File("./target/"+jsxPath);
	File destinationJSXFile = new File(tmp+jsxPath);
	FileUtils.copyFile(originalJSXFile, destinationJSXFile);

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:"+tmp);

	Map<String, String> params = MorfeuUtils.paramStringMap(GenericHttpServlet.POST_VALUE, content);
	String result = SPFileContentControlModule.post(config).apply(pathElems, params);
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
		() -> assertTrue(jsx.contains("<Data number=\"32\"/>"), "Incorrect data node"),
		() -> assertFalse(jsx.contains("number=\"42\""), "Incorrect data2 nodes"),
		() -> assertFalse(jsx.contains("text=\"blahblah\""), "Incorrect data2 nodes"),
		() -> assertTrue(jsx.contains("<Data2 number=\"32\" text=\"blahbla4\"/>"), "Incorrect data2 nodes"),
		() -> assertTrue(jsx.endsWith("ReactDOM.render(slot1, document.getElementById('root'));\n"), "Incorrect ending")
	);

}


@Test @DisplayName("Save JSX content precision")
public void testSaveJSXPrecision() throws Exception {

	setupPathElements("classes/test-resources/documents/example-3.jsx");

	File contentFile = new File("./target/classes/test-resources/documents/example-3-edit.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);

	// we copy the original JSX source to the temp folder so new content can be injected and modified
	File originalJSXFile = new File("./target/"+jsxPath);
	File destinationJSXFile = new File(tmp+jsxPath);
	FileUtils.copyFile(originalJSXFile, destinationJSXFile);

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:"+tmp);

	Map<String, String> params = MorfeuUtils.paramStringMap(GenericHttpServlet.POST_VALUE, content);
	String result = SPFileContentControlModule.post(config).apply(pathElems, params);
	// check operation result first
	assertAll("check result",
		() -> assertNotNull(result),
		() -> assertFalse(result.contains("KO"))
	);

	String jsx = FileUtils.readFileToString(destinationJSXFile, Config.DEFAULT_CHARSET);
	//System.err.println(jsx);
	assertAll("check jsx output",
			() -> assertTrue(jsx.startsWith("const a ="), "Did not start correctly"),
			() -> assertTrue(jsx.contains("<Stuff>a</Stuff>"), "Does not inject code")
	);

}


@Test @DisplayName("Generate JSX content with {} expressions")
public void testGenerateJSXContentFiltered() throws Exception {

	setupPathElements("classes/test-resources/documents/filtered.jsx");

	Properties config = new Properties();
	config.put(MorfeuServlet.RESOURCES_PREFIX, "file:./target/");

	String content = SPFileContentControlModule.get(config).apply(pathElems, MorfeuUtils.emptyParamStringMap());
	//System.err.println(content);
	assertNotNull(content);
	compareWithXMLFile(content, "./target/classes/test-resources/documents/filtered.xml");

}


private void setupPathElements(String path) {

	jsxPath = path;
	pathElems = new ArrayList<String>(0);
	pathElems.add("/content/"+jsxPath);
	pathElems.add(jsxPath);
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

