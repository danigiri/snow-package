package cat.calidos.snowpackage.model.injection;

import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.snowpackage.model.CellSlot;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SPCellSlotInjectorModule {


@Produces
String code(List<CellSlot> codeSlots,  @Named("Code") String code) {
	return DaggerViewComponent.builder()
								.withTemplatePath("templates/cellslots-to-jsx.twig")
								.withValue(MorfeuUtils.paramMap("cellslots", codeSlots,
																"code", code))
								.build()
								.render();
}


@Produces
List<CellSlot> codeSlots(org.w3c.dom.Document doc) {

	List<CellSlot> codeSlots = new ArrayList<CellSlot>();

	List<Node> pendingCodeSlotNodes = new LinkedList<Node>();
	pendingCodeSlotNodes = addChildrenToList(doc, pendingCodeSlotNodes);
	while (!pendingCodeSlotNodes.isEmpty()) {
		Node node = pendingCodeSlotNodes.remove(0);
		if (node.getNodeName().equals("codeslot")) {
			StringBuffer content = new StringBuffer();
			NodeList children = node.getChildNodes();
			for (int i=0; i< children.getLength(); i++) {
				content.append(children.item(i));
			}
			codeSlots.add(new CellSlot(node, content.toString()));
		} else {
			pendingCodeSlotNodes = addChildrenToList(node, pendingCodeSlotNodes);
		}
	}

	return codeSlots;

}


private List<Node> addChildrenToList(Node node, List<Node> list) {

	NodeList rootCodeSlotNodes = node.getChildNodes();
	for (int i = 0; i<rootCodeSlotNodes.getLength(); i++) {
		list.add(rootCodeSlotNodes.item(i));
	}
	return list;
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

