package cat.calidos.snowpackage.model.injection;

import dagger.BindsInstance;
import dagger.Component;

import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Named;

import cat.calidos.morfeu.utils.injection.ConfigurationModule;

/**
 * Take a piece of code, parse it for code slots
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = { SPCellSlotParserModule.class, ConfigurationModule.class })
public interface SPCellSlotParserComponent {

public static String	JSX					= "jsx";
public static String	DEFAULT_LOAD_FILTER	= "replace{\"replacements\":"
		+ "{\"from\":\"=\\\\s*\\\\{([^}]*?)}\",\"to\":\"=\\\"{$1}\\\"\"}}";
public static String TSNODE_PROPERTY = "tsnode";
public static String TSNODE_PATH = "src/main/angular/node_modules/.bin/ts-node";
public static String TSCODE_PROPERTY = "tscode";
public static String TSCODE_PATH = "./src/main/angular/src/app/snow-package.ts";
public static String NODEFOLDER_PROPERTY = "nodefolder";
public static String NODEFOLDER = "/usr/local/bin";



@Named("Slots")
String slots();

@Named("CodeSlots")
String codeSlots();

@Named("Content")
String content();

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder withCode(String code);
	@BindsInstance Builder fromPath(@Named("Path") String path);
	@BindsInstance Builder filters(@Named("filters") String filters);
	@BindsInstance Builder withProperties(@Nullable @Named("InputProperties") Properties properties);

	SPCellSlotParserComponent build();

}
//@formatter:on

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
