package cat.calidos.snowpackage.model.injection;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.DaggerXMLNodeToStringComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.snowpackage.model.CellSlot;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SPCellSlotInjectorModule {


@Produces
public static String code(List<CellSlot> codeSlots,  @Named("Code") String code) {
	return DaggerViewComponent.builder()
								.withTemplatePath("templates/cellslots-to-jsx.twig")
								.withValue(MorfeuUtils.paramMap("cellslots", codeSlots, "code", code))
								.build()
								.render();
}


@Produces
public static List<CellSlot> codeSlots(org.w3c.dom.Document doc) throws ConfigurationException, ParsingException {

	List<CellSlot> codeSlots = new ArrayList<CellSlot>();

	List<Node> pendingCodeSlotNodes = new LinkedList<Node>();
	pendingCodeSlotNodes = addChildrenToList(doc, pendingCodeSlotNodes);
	while (!pendingCodeSlotNodes.isEmpty()) {
		Node node = pendingCodeSlotNodes.remove(0);
		if (node.getNodeName().equals("codeslot")) {
			String content = nodeContent(node);
			codeSlots.add(new CellSlot(node, content));
		} else {
			pendingCodeSlotNodes = addChildrenToList(node, pendingCodeSlotNodes);
		}
	}

	return codeSlots;

}


private static String nodeContent(Node node) throws ConfigurationException, ParsingException {

	// TODO: here we could check the indentation and shift to the left

	StringBuffer contentBuffer = new StringBuffer();
	NodeList children = node.getChildNodes();
	for (int i=0; i< children.getLength(); i++) {
		try {
		contentBuffer.append(DaggerXMLNodeToStringComponent.builder().fromNode(children.item(i)).build().xml().get());
		} catch (InterruptedException|ExecutionException e) {
			throw new ParsingException("Some kind of problem on node generation", e);
		}
	}

	// now we remove the leading whitespace, assuming that any whitespace leading to the first element
	// was not added by the end user
	//          <foo>
	//                  <bar>yo</bar>
	//          </foo>
	// should turn into
	// <foo>
	//          <bar>yo</bar>
	// </foo>
	String content = contentBuffer.toString();
	int w = countWhitespace(content);
	
	content = content.lines().map(l -> replaceWhitespace(l, w)).collect(Collectors.joining("\n"));
	
	return content;

}


private static List<Node> addChildrenToList(Node node, List<Node> list) {

	NodeList rootCodeSlotNodes = node.getChildNodes();
	for (int i = 0; i<rootCodeSlotNodes.getLength(); i++) {
		list.add(rootCodeSlotNodes.item(i));
	}

	return list;

}


private static int countWhitespace(String content) {

	int len = content.length();
	if (len==0) {
		return 0;
	}

	int i = 0;
	char c = content.charAt(0);
	while (++i<len && (c=='\n' || c==' ' || c=='\t')) {
			c = content.charAt(i);
	}

	return i-1;

}


private static String replaceWhitespace(String s, int count) {

	int i = 0;
	while (i<count && i<s.length() && (s.charAt(i)==' ' || s.charAt(i)=='\t')) {
		i++;
	}

	return s.substring(i);

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

