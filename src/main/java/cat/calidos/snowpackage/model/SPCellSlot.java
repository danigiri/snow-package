package cat.calidos.snowpackage.model;

import org.w3c.dom.Node;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellSlot {

private String type;
private int start;
private int end;
private String content;


public CellSlot(Node node, String content) {

	this.content = content;
	this.type = node.getAttributes().getNamedItem("type").getNodeValue();
	int startLine = Integer.parseInt(node.getAttributes().getNamedItem("start_line").getNodeValue());
	int startCol = Integer.parseInt(node.getAttributes().getNamedItem("start_column").getNodeValue());
	int endLine = Integer.parseInt(node.getAttributes().getNamedItem("start_line").getNodeValue());
	int endCol = Integer.parseInt(node.getAttributes().getNamedItem("start_column").getNodeValue());
	this.start = startLine;
	this.end = Integer.parseInt(node.getAttributes().getNamedItem("end").getNodeValue());

}


public String getType() {

	return type;
}


public int getStart() {
	return start;
}


public int getEnd() {
	return end;
}


public String getContent() {
	return content;
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

