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
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.snowpackage.SPTezt;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPFileToContentModuleTest extends SPTezt {


@Test @DisplayName("Generate code slots")
public void testGenerateCodeSlots() throws Exception {

	ArrayList<String> pathElems = new ArrayList<String>(0);
	pathElems.add("classes/test-resources/documents/example-1.jsx");
	
	Map<String, String> configMap = MorfeuUtils.paramStringMap(MorfeuServlet.RESOURCES_PREFIX, "file:./target/");
	Properties config = new Properties();
	config.putAll(configMap);

	String content = SPFileToContentModule.get(config).apply(pathElems, MorfeuUtils.emptyParamStringMap());
	System.err.println(content);
	assertAll("basic checks",
			() -> assertNotNull(content)
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

