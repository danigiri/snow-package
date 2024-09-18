package cat.calidos.snowpackage.model.injection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import dagger.Provides;
import dagger.Module;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.filter.injection.DaggerFilterComponent;
import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.RunningTask;
import cat.calidos.morfeu.runtime.api.StartingTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.runtime.injection.DaggerExecTaskComponent;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.snowpackage.model.SPCellSlot;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SPCellSlotParserModule {

public static final String CODE_PARAM = "code";

protected final static Logger log = LoggerFactory.getLogger(SPCellSlotParserModule.class);

private static final String JSX = "jsx";
private static final String TSX = "tsx";
private static final String PATH = "/slots/(jsx|tsx)/?";

public static String TSNODE_PROPERTY = "tsnode";
public static String TSNODE_PATH = "src/main/angular/node_modules/.bin/ts-node";
public static String TSCODE_PROPERTY = "tscode";
public static String TSCODE_PATH = "./src/main/angular/src/app/snow-package.ts";
public static String NODEFOLDER_PROPERTY = "nodefolder";
public static String NODEFOLDER = "/usr/local/bin";

private static final String OUTPUT_ERROR = "";
private static final int TIMEOUT = 20000;

/*

 cat code | hexdump -v -e '/1 "%02x"' | sed 's/\(..\)/%\1/g' > codeencoded
 edit codeencoded and add 'code=' at the beginning
  wget -d -S -O - --post-file=./codeencoded http://localhost:8990/dyn/slots/jsx/
*/


@Provides @Named("JSXTask")
public static ReadyTask runJSX(@Named("Configuration") Properties config) {

	String nodeFolder = config.getProperty(NODEFOLDER_PROPERTY, NODEFOLDER);
	String tsNodeCommand = config.getProperty(TSNODE_PROPERTY, TSNODE_PATH);
	String tsCode = config.getProperty(TSCODE_PROPERTY, TSCODE_PATH);
	var command = "PATH=$PATH:"+nodeFolder+" "+ tsNodeCommand+" "+tsCode+" --jsx";

	return DaggerExecTaskComponent.builder()
									.exec( "/bin/bash", "-c", command)
									.type(Task.ONE_TIME)
									.startedMatcher(s -> Task.NEXT)
									.problemMatcher(s -> true)	// if anything shows on STDERR
									.build()
									.readyTask();
}


@Provides @Named("Slots")
public static String slots(@Named("JSXTask") ReadyTask task, String code) {

	StartingTask start = task.start(code);
	RunningTask running = start.runningTask();

	try {
		running.spinUntil(Task.FINISHED, TIMEOUT);
		FinishedTask finished = running.finishedTask();

		return (finished.isOK() && finished.result()==0) ? start.show()+running.show() : OUTPUT_ERROR;

	} catch (InterruptedException e) {
		return OUTPUT_ERROR;
	}

}


@Provides @Named("CodeSlots") 
public static String codeSlots(@Named("Slots") String slots, String code, @Named("filters") String filters) {

	String out = "";
	try {
		JsonNode slotsJSON = DaggerJSONParserComponent.builder().from(slots).build().json().get();
		List<SPCellSlot> createdSlots = new ArrayList<SPCellSlot>(slotsJSON.size());
		slotsJSON.iterator()
					.forEachRemaining(j -> createdSlots.add(
						DaggerSPCellSlotComponent.builder().with(code).json(j).build().slot()
					));
		Map<String, Object> v = MorfeuUtils.paramMap("cellSlots", createdSlots, "code", code);
		String template = "cellslots-to-codeslots.ftl";
		out = DaggerViewComponent.builder().withTemplatePath(template).withValue(v).build().render();
	} catch (Exception e) {
		throw new RuntimeException("Slots parsed had incorrect structure", e);
	}

	if (!filters.isEmpty()) {
		try {
			out = DaggerFilterComponent.builder().filters(filters).build().stringToString().get().apply(out);
		} catch (Exception e) {
			log.error("Could not apply filters to code slots {}", e.getMessage());
			e.printStackTrace();
		}
	}

	return out;

}


@Provides @Named("Content") 
public static String content(@Named("CodeSlots") String codeSlots, String code,	@Named("Path") String path) {
	return DaggerViewComponent.builder()
									.withTemplatePath("codeslots-to-xml.ftl")
									.withValue(MorfeuUtils.paramStringMap("codeslots", codeSlots, "path", path))
									.build()
									.render();
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

