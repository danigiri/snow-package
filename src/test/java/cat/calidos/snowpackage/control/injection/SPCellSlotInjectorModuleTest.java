package cat.calidos.snowpackage.control.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerXMLParserComponent;
import cat.calidos.snowpackage.model.CellSlot;
import cat.calidos.snowpackage.model.injection.SPCellSlotInjectorModule;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPCellSlotInjectorModuleTest {

String jsx;


@BeforeEach
public void setup() {
	jsx = 	"012345678<012>\n" +		// 15	code:<0, start[0]=9>				start[0]=9,end[0]=46	15
			"	<234 6789=\"23\">\n" +	// 17																32
			"	</345>\n" +				// 8 																40
			"</234>;7890\n" +			// 12  	code:<end[0]=46, start[1]=61>								52
			"012345678<012>\n" +		// 15 										start[1]=61,end[1]=98	67
			"	<234 6789=\"23\">\n" +	// 17																84
			"	</345>\n" +				// 8																92
			"</234>;7890\n"+			// 12	code:<end[1]+1=99, len>										104
			"0123456789012345678901234567890123456789012345678901\n"; // 53									157

}

@Test @DisplayName("Test inject identical code")
public void testGenerateCodeIdentity() throws Exception {

	File contentFile = new File("./target/classes/test-resources/documents/example-3.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);

	Document doc = DaggerXMLParserComponent.builder().withContent(content).build().document().get();
	List<CellSlot> slots = SPCellSlotInjectorModule.codeSlots(doc);

	String exp = 	"012345678<Row>\n" + 
					"	<Col size=\"12\">\n" + 
					"	</Col>\n" + 
					"</Row>;7890\n" + 
					"012345678<Row>\n" + 
					"	<Col size=\"12\"><Data number=\"1\"/></Col>\n" + 
					"</Row>;7890\n" + 
					"0123456789012345678901234567890123456789012345678901\n";
	String code = SPCellSlotInjectorModule.code(slots, jsx);
	assertEquals(exp, code);
	//System.out.println(jsx);
	//System.err.println(code);

}


@Test @DisplayName("Test inject modified code")
public void testGenerateCodeModified() throws Exception {

	File contentFile = new File("./target/classes/test-resources/documents/example-3-edit.xml");
	String content = FileUtils.readFileToString(contentFile, Config.DEFAULT_CHARSET);

	Document doc = DaggerXMLParserComponent.builder().withContent(content).build().document().get();
	List<CellSlot> slots = SPCellSlotInjectorModule.codeSlots(doc);
	String code = SPCellSlotInjectorModule.code(slots, jsx);
	System.out.println(jsx);
	System.err.println(code);
}


/*

Identity example


const a =<row>
012345678<012>

	<col size="12">
	<234 6789="23">

	</col>
	</345>

</row>;//47
</234>;7890

const b =<row>
012345678<012>

	<col size="12">
	<234 6789="23">

	</col>
	</345>

</row>;//99
</234>;7890

ReactDOM.render(b, document.getElementById('root'));
0123456789012345678901234567890123456789012345678901

 */

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

