package cat.calidos.snowpackage.control.injection;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

import javax.inject.Named;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.RunningTask;
import cat.calidos.morfeu.runtime.api.StartingTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.runtime.injection.DaggerExecTaskComponent;
import dagger.Provides;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SPSlotParserModule {

private static final String CODE_PARAM = "code";

protected final static Logger log = LoggerFactory.getLogger(SPSlotParserModule.class);

private static final String JSX = "jsx";
private static final String TSX = "tsx";
private static final String PATH = "/slots/(jsx|tsx)/?";

protected static String TSNODE_PROPERTY = "tsnode";
protected static String TSNODE_PATH = "/usr/local/bin/ts-node";
protected static String TSCODE_PROPERTY = "tscode";
protected static String TSCODE_PATH = "./src/main/angular/src/app/snow-package.ts";
protected static String NODEFOLDER_PROPERTY = "nodefolder";
protected static String NODEFOLDER = "/usr/local/bin";

private static final String OUTPUT_ERROR = "";
private static final int TIMEOUT = 2000;

/*

 cat code | hexdump -v -e '/1 "%02x"' | sed 's/\(..\)/%\1/g' > codeencoded
 edit codeencoded and add 'code=' at the beginning
  wget -d -S -O - --post-file=./codeencoded http://localhost:8990/dyn/slots/jsx/
*/

@Provides @IntoMap @Named("POST")
@StringKey(PATH)
public static BiFunction<List<String>, Map<String, String>, String> postCode(@Named("JSX") ReadyTask task) {


	return (pathElems, params) -> {

		String lang = pathElems.get(1);
		String withCode = params.get(CODE_PARAM);
		log.warn("SPSlotParserModule::postCode() [{},{}, {}]", lang, withCode, params);
		if (!lang.equalsIgnoreCase(JSX) || withCode==null) {
			return OUTPUT_ERROR;
		}

		return run(task, withCode);

	};

}


@Provides @Named("JSX")
public static ReadyTask runJSX(@Named("Configuration") Properties config) {

	String nodeFolder = config.getProperty(NODEFOLDER_PROPERTY, NODEFOLDER);
	String tsNodeCommand = config.getProperty(TSNODE_PROPERTY, TSNODE_PATH);
	String tsCode = config.getProperty(TSCODE_PROPERTY, TSCODE_PATH);
	String command = "PATH=$PATH:"+nodeFolder+" "+ tsNodeCommand+" "+tsCode+" --jsx";

	return DaggerExecTaskComponent.builder()
									.exec( "/bin/bash", "-c", command)
									.type(Task.ONE_TIME)
									.startedMatcher(s -> Task.NEXT)
									.problemMatcher(s -> true)	// if anything shows on STDERR
									.build()
									.readyTask();
}


private static String run(ReadyTask task, String code) {

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

