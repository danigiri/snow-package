package cat.calidos.snowpackage;

import org.junit.jupiter.api.BeforeEach;

import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.RunningTask;
import cat.calidos.morfeu.runtime.api.StartingTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.runtime.injection.DaggerExecTaskComponent;
import cat.calidos.morfeu.utils.Tezt;
import cat.calidos.snowpackage.model.injection.SPCellSlotParserComponent;


/**
 * Run TS code as a sub-process for testing
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class SPTezt extends Tezt {

protected static String	DEFAULT_URL				= "http://localhost:4200";
protected static String	DEFAULT_WEBAPP_BASE_URL	= DEFAULT_URL + "/";
// we reuse properties from the module for the test, to avoid drift, though they can still
// be overriden
protected static String		TSNODE_PROPERTY		= SPCellSlotParserComponent.TSNODE_PROPERTY;
protected static String		TSNODE_PATH			= SPCellSlotParserComponent.TSNODE_PATH;
protected static String		TSCODE_PROPERTY		= SPCellSlotParserComponent.TSCODE_PROPERTY;
protected static String		TSCODE_PATH			= SPCellSlotParserComponent.TSCODE_PATH;
protected static String		NODEFOLDER_PROPERTY	= SPCellSlotParserComponent.NODEFOLDER_PROPERTY;
protected static String		NODEFOLDER			= SPCellSlotParserComponent.NODEFOLDER;
private static final String	JSX					= "jsx";

// private SPUITezt tezt;
protected String	nodeFolder;
protected String	tsNodeCommand;
protected String	tsCode;

@BeforeEach
public void setup() {

	nodeFolder = defineSystemVariable(NODEFOLDER_PROPERTY, NODEFOLDER);
	tsNodeCommand = defineSystemVariable(TSNODE_PROPERTY, TSNODE_PATH);
	tsCode = defineSystemVariable(TSCODE_PROPERTY, TSCODE_PATH);

	// open(appBaseURL);
	// tezt = new SPUITezt();

}


public String runCode(String code) throws Exception {
	return runCodeWithFormat(code, JSX);
}


private String runCodeWithFormat(	String code,
									String format)
		throws Exception {

	String command = "PATH=$PATH:" + nodeFolder + " " + tsNodeCommand + " " + tsCode + " --"
			+ format;
	ReadyTask task = DaggerExecTaskComponent
			.builder()
			.exec("/bin/bash", "-c", command)
			.type(Task.ONE_TIME)
			.startedMatcher(s -> Task.NEXT)
			.problemMatcher(s -> true) // if anything shows on STDERR
			.build()
			.readyTask();
	StartingTask start = task.start(code);
	RunningTask running = start.runningTask();
	running.spinUntil(Task.FINISHED);

	FinishedTask finished = running.finishedTask();
	if (finished.failed() || finished.result() != 0) {
		throw new Exception(
				"Could not run code, isOK:" + finished.isOK() + ", result:" + finished.result());
	}

	return start.show() + running.show();

}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
