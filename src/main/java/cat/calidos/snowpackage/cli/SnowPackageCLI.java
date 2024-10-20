package cat.calidos.snowpackage.cli;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cat.calidos.morfeu.cli.MorfeuBaseCLI;


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

@Option(names = "--prefix", description = "model to use (default is file://<cwd>)")
String prefix;

@Parameters(description = "command {parse|}")
String command;

@Parameters(description = "content to parse")
String path;

@Override
public Integer call() throws Exception {

	if (command.equalsIgnoreCase(PARSE)) {

		if (!quiet) {
			// System.out.println(output);
		}
	}
	// TODO Auto-generated method stub
	return null;
}


public static void main(String[] args) {
	System.exit(SnowPackageCLI.mainImpl(new SnowPackageCLI(), args).getLeft());
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
