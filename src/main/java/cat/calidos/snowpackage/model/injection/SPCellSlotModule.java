package cat.calidos.snowpackage.model.injection;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.injection.DaggerXMLNodeToStringComponent;
import cat.calidos.snowpackage.model.SPCellSlot;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SPCellSlotModule {

@Provides
public static SPCellSlot slot(@Named("type") String type,
								@Named("start") int start,
								@Named("end") int end,
								@Named("content") String content) {
	return new SPCellSlot(type, start, end, content);
}


@Provides
public static @Named("type") String type(@Nullable Node node, @Nullable JsonNode jsonNode) {
	return node!=null ? node.getAttributes().getNamedItem("type").getNodeValue() : jsonNode.get("type").asText();
}


@Provides
public static @Named("content") String content(@Nullable Node node) {

	if (node==null) {
		return "";
	}

	// TODO: here we could check the indentation and shift to the left

	var contentBuffer = new StringBuffer();
	NodeList children = node.getChildNodes();
	for (int i=0; i< children.getLength(); i++) {
		try {
		contentBuffer.append(DaggerXMLNodeToStringComponent.builder().fromNode(children.item(i)).build().xml().get());
		} catch (ConfigurationException|ParsingException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException|ExecutionException e) {
			throw new RuntimeException(new ParsingException("Some kind of problem on node generation", e));
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


@Provides
public static @Named("start") int start(@Nullable Node node,
										List<String> lines,
										List<Integer> lineCounts,
										@Nullable JsonNode jsonNode) {

	int start;

	if (node!=null) {
		start = Integer.parseInt(node.getAttributes().getNamedItem("start").getNodeValue());
	} else {
		int linesToCount = jsonNode.get("start_line").asInt()-1;	// starts at 1, we don't count the last line so -1
		int startCol = jsonNode.get("start_column").asInt();
		// we add lines to count as we want to add the \n's
		start = lineCounts.stream().limit(linesToCount).mapToInt(Integer::intValue).sum()+linesToCount+startCol;
	}

	return start;

}


@Provides
public static @Named("end") int end(@Nullable Node node,
									List<String> lines,
									List<Integer> lineCounts,
									@Nullable JsonNode jsonNode) {

	int end;

	if (node!=null) {
		end = Integer.parseInt(node.getAttributes().getNamedItem("end").getNodeValue());
	} else {
		int linesToCount = jsonNode.get("end_line").asInt()-1;	// starts at 1, we don't count the last line so -1
		int endCol = jsonNode.get("end_column").asInt();
		// we add lines to count as we want to add the \n's
		end = lineCounts.stream().limit(linesToCount).mapToInt(Integer::intValue).sum()+linesToCount+endCol;
	}

	return end;

}


@Provides
public static List<String> lines(String code) {
	return code.lines().collect(Collectors.toUnmodifiableList());
}


@Provides
public static List<Integer> lineCounts(List<String> lines) {
	return lines.stream().map(l -> l.length()).collect(Collectors.toUnmodifiableList());
}


private static int countWhitespace(String content) {

	int len = content.length();
	if (len==0) {
		return 0;
	}

	var i = 0;
	char c = content.charAt(0);
	while (++i<len && (c=='\n' || c==' ' || c=='\t')) {
			c = content.charAt(i);
	}

	return i-1;

}


private static String replaceWhitespace(String s, int count) {

	var i = 0;
	while (i<count && i<s.length() && (s.charAt(i)==' ' || s.charAt(i)=='\t')) {
		i++;
	}

	return s.substring(i);

}

}

/*
 *    Copyright 2024 Daniel Giribet
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

