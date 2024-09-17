package cat.calidos.snowpackage.model.injection;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cat.calidos.morfeu.filter.injection.DaggerFilterComponent;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.snowpackage.model.SPCellSlot;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SPCellSlotInjectorModule {


@Produces
public static String code(List<SPCellSlot> codeSlots,
							@Named("Code") String code,
							@Named("filters") String filters,
							Map<String, Object> params) throws ConfigurationException, ParsingException{

	String jsx = DaggerViewComponent.builder()
										.withTemplatePath("cellslots-to-jsx.ftl")
										.withValue(params)
										.build()
										.render();
	if (!filters.isEmpty()) {
		try {
			jsx = DaggerFilterComponent.builder().filters(filters).build().stringToString().get().apply(jsx);
		} catch (Exception e) {
			throw new ParsingException("Problem trying to apply save filters to code", e);
		}
	}

	return jsx;

}


@Produces
public static List<SPCellSlot> codeSlots(org.w3c.dom.Document doc,  @Named("Code") String code)
											throws ConfigurationException, ParsingException {

	List<SPCellSlot> codeSlots = new ArrayList<SPCellSlot>();
	List<Node> pendingCodeSlotNodes = new LinkedList<Node>();
	pendingCodeSlotNodes = addChildrenToList(doc, pendingCodeSlotNodes);
	while (!pendingCodeSlotNodes.isEmpty()) {
		Node node = pendingCodeSlotNodes.remove(0);
		if (node.getNodeName().equals("codeslot")) {
			codeSlots.add(DaggerSPCellSlotComponent.builder().with(code).node(node).build().slot());
		} else {
			pendingCodeSlotNodes = addChildrenToList(node, pendingCodeSlotNodes);
		}
	}

	return codeSlots;

}

@Produces
public static Map<String, Object> params(List<SPCellSlot> codeSlots, @Named("Code") String code) {
	// setting the size and end here instead of in the template as freemarker is complaining
	int size = codeSlots.size();
	return MorfeuUtils.paramMap("cellslots", codeSlots, 
								"code", code,
								"size", size,
								"end", size>0 ? codeSlots.get(0).getStart() : 0);
}


private static List<Node> addChildrenToList(Node node, List<Node> list) {

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

