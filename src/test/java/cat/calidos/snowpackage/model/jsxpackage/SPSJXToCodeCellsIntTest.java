package cat.calidos.snowpackage.model.jsxpackage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.snowpackage.SPTezt;
import cat.calidos.snowpackage.model.DaggerSPCellSlotParserComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPSJXToCodeCellsIntTest extends SPTezt {


@Test @DisplayName("Apply template filter test")
public void applyTemplateTest() throws Exception {

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
			"const element2 = <h2>All is fine</h2>;\n" + 
			"ReactDOM.render(element, document.getElementById('root'));\n" +
			"ReactDOM.render(element2, document.getElementById('root'));";
	String slots = DaggerSPCellSlotParserComponent.builder().withCode(code).build().slots(); 
	JsonNode cellSlots = DaggerJSONParserComponent.builder().from(slots).build().json().get();
	Map<String, Object> v = MorfeuUtils.paramMap("cellSlots", cellSlots, "code", code);
	String template = "templates/cellslots-to-codeslots.twig";
	String codeSlots = DaggerViewComponent.builder().withTemplatePath(template).withValue(v).build().render();

	assertAll("checking slots",
		() -> assertNotNull(codeSlots, "Should get a codeslots output"),
		() -> assertTrue(codeSlots.contains("<h1>Hello, {formatName(user)}!</h1>")),
		() -> assertTrue(codeSlots.contains("<h2>All is fine</h2>"))
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

