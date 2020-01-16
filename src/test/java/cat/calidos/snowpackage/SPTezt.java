package cat.calidos.snowpackage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.RunningTask;
import cat.calidos.morfeu.runtime.api.StartingTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.runtime.injection.DaggerExecTaskComponent;
import cat.calidos.morfeu.utils.Tezt;
import cat.calidos.morfeu.webapp.UITezt;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class SPTezt extends Tezt {

protected static String DEFAULT_URL = "http://localhost:4200";
protected static String DEFAULT_WEBAPP_BASE_URL = DEFAULT_URL+"/";
protected static String TSNODE_PROPERTY = "tsnode";
protected static String TSNODE_PATH = "/usr/local/bin/ts-node";
protected static String TSCODE_PROPERTY = "tscode";
protected static String TSCODE_PATH = "./src/main/angular/src/app/snow-package.ts";
protected static String NODEFOLDER_PROPERTY = "nodefolder";
protected static String NODEFOLDER = "/usr/local/bin";

private SPUITezt tezt;
private String nodeFolder;
private String tsNodeCommand;
private String tsCode;


@BeforeEach
public void setup() {

	nodeFolder = defineSystemVariable(NODEFOLDER_PROPERTY, NODEFOLDER);
	tsNodeCommand = defineSystemVariable(TSNODE_PROPERTY, TSNODE_PATH);
	tsCode = defineSystemVariable(TSCODE_PROPERTY, TSCODE_PATH);
	
//	open(appBaseURL);
//	tezt = new SPUITezt();

}


@Test @DisplayName("Test using UI form component") @Disabled
public void testCodeThroughUI() {

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
		"ReactDOM.render(element, document.getElementById('root'));";

	tezt.setInput(code);
	Optional<String> outputOpt = tezt.run();
	assertTrue(outputOpt.isPresent());
	String slot = outputOpt.get();
	assertAll("",
		() -> assertNotNull(slot),
		() -> assertTrue(slot.startsWith("<slot")),
		() -> assertTrue(slot.endsWith("/>")),
		() -> assertTrue(slot.contains("name=\"___fragment\"")),
		() -> assertTrue(slot.contains("start=\"164\" ")),
		() -> assertTrue(slot.contains("end=\"270\"/>"))
	);
	
}


@Test @DisplayName("Test code using ts-node")
public void testCode() throws Exception {

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
			"ReactDOM.render(element, document.getElementById('root'));";
	
	String command = "PATH=$PATH:"+nodeFolder+" "+ tsNodeCommand+" "+tsCode;
	ReadyTask task = DaggerExecTaskComponent.builder()
			.exec( "/bin/bash", "-c", command)
			.type(Task.ONE_TIME)
			.withStdin(code)
			.startedMatcher(s -> Task.NEXT)
			.problemMatcher(s -> false)	// if anything shows on STDERR
			.build()
			.readyTask();
	StartingTask start = task.start();
//	System.err.println(start.show());
	RunningTask running = start.runningTask();
	running.spinUntil(Task.FINISHED);
	System.err.println(running.show());
	
	FinishedTask finished = running.finishedTask();
	assertTrue(finished.isOK());
	assertEquals(0, finished.result());
}

}

/*
 *	  Copyright 2020 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */


