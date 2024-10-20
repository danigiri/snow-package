package cat.calidos.snowpackage.cli;

import java.util.Properties;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cat.calidos.morfeu.cli.MorfeuBaseCLI;
import cat.calidos.snowpackage.control.injection.DaggerSPContentGETControlComponent;
import cat.calidos.snowpackage.model.injection.SPCellSlotParserComponent;
import cat.calidos.snowpackage.model.injection.SPCellSlotParserModule;


/**
 * Utility CLI helpful for diagnostics
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@Command(name = "SnowPackageCLI", version = "SnowPackageCLI 0.8", mixinStandardHelpOptions = true)
public class SnowPackageCLI extends MorfeuBaseCLI implements Callable<Integer> {

public static final String PARSE = "parse";

@Option(names = { "-q", "--quiet" }, description = "do not print anything")
boolean quiet = false;

@Option(names = { "-v", "--verbose" }, description = "verbose diagnosptic output")
boolean verbose = false;

@Option(names = { "-p", "--prefix" }, description = "model to use (default is file://<cwd>)")
String prefix;

@Option(names = "--filters", description = "custom filters to apply (otherwise defaults will be used")
String filters;

@Option(names = { "-t", "--tsnode" }, description = "path to ts-node executable")
String tsnodePath;

@Option(names = { "-c ", "--tscode" }, description = "path to ts code")
String tscodePath;

@Parameters(description = "command {parse|}")
String command;

@Parameters(description = "content to parse")
String path;

@Override
public Integer call() throws Exception {
	var config = new Properties();
	config = handlePropertyConfiguration(
			config,
			SPCellSlotParserComponent.TSNODE_PROPERTY,
			tsnodePath);
	config = handlePropertyConfiguration(
			config,
			SPCellSlotParserComponent.TSCODE_PROPERTY,
			tscodePath);
	prefix = prefix == null ? "file://" + System.getProperty("user.dir") : prefix;
	if (verbose) {
		System.err.println("Using prefix='" + prefix + "'");
	}

	if (command.equalsIgnoreCase(PARSE)) {

		filters = filters == null ? SPCellSlotParserComponent.DEFAULT_LOAD_FILTER : filters;
		output = DaggerSPContentGETControlComponent
				.builder()
				.fromPath(path)
				.withPrefix(prefix)
				.filters(filters)
				.withProperties(config)
				.andProblem("")
				.build()
				.parsedCode();
		if (!quiet) {
			System.out.println(output);
		}
	}
	return 0;
}


public static void main(String[] args) {
	System.exit(SnowPackageCLI.mainImpl(new SnowPackageCLI(), args).getLeft());
}


private Properties handlePropertyConfiguration(	Properties properties,
												String propertyName,
												String value) {
	if (value != null) {
		properties.put(propertyName, value);
	}
	if (verbose) {
		var msg = "Using " + propertyName + "'" + (tsnodePath == null ? value : "<DEFAULT>") + "'";
		System.err.println(msg);
	}
	return properties;
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
