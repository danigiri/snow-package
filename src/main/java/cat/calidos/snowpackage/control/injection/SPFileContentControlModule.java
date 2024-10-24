package cat.calidos.snowpackage.control.injection;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiFunction;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

import javax.inject.Named;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.control.injection.DaggerOperationResultComponent;
import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.filter.injection.DaggerFilterComponent;
import cat.calidos.morfeu.utils.injection.DaggerSaverComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.snowpackage.model.injection.DaggerSPCellSlotInjectorComponent;
import cat.calidos.snowpackage.model.injection.SPCellSlotInjectorComponent;
import cat.calidos.snowpackage.model.injection.SPCellSlotParserComponent;


/**
 * We take a file path from the request, read it and turn it into a document
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SPFileContentControlModule {

protected final static Logger log = LoggerFactory.getLogger(SPFileContentControlModule.class);

private static final String	PATH				= "/content/(.+\\.jsx)";
private static final String	PROBLEM				= "";
private static final String	FILTERS_PARAM		= "filters";										// applied
																									// just
																									// before
																									// returning
																									// the
																									// content
private static final String	DEFAULT_LOAD_FILTER	= SPCellSlotParserComponent.DEFAULT_LOAD_FILTER;
private static final String	DEFAULT_SAVE_FILTER	= SPCellSlotInjectorComponent.DEFAULT_SAVE_FILTER;

private static String prefix;

@Provides @IntoMap @Named("GET") @StringKey(PATH)
public static BiFunction<List<String>, Map<String, String>, String> get(@Named("Configuration") Properties config) {

	prefix = (String) config.get(MorfeuServlet.RESOURCES_PREFIX);

	return (pathElems,
			params) -> {

		String path = pathElems.get(1); // jsx file path
		String filters = params.containsKey(FILTERS_PARAM) ? params.get(FILTERS_PARAM)
				: DEFAULT_LOAD_FILTER;

		log.trace("Getting code slots for [{}]/{}", prefix, path);
		String code = DaggerSPContentGETControlComponent
				.builder()
				.fromPath(path)
				.withPrefix(prefix)
				.filters(filters)
				.withProperties(config)
				.andProblem(PROBLEM)
				.build()
				.parsedCode();
		return code;
	};
}


@Provides @IntoMap @Named("POST") @StringKey(PATH)
public static BiFunction<List<String>, Map<String, String>, String> post(@Named("Configuration") Properties config) {

	prefix = (String) config.get(MorfeuServlet.RESOURCES_PREFIX);

	return (pathElems,
			params) -> {

		long before = System.currentTimeMillis();
		String problem = null;

		String path = pathElems.get(1); // normalised already
		String fullPath = prefix + path;
		String content = params.get("content");
		if (content == null) {
			content = params.get(GenericHttpServlet.POST_VALUE);
		}
		if (content == null) {
			log.warn("Content for '" + path + "' is missing, will save empty content");
		}

		// it's unclear why we apply for filters separately here, with no default =0
		String filters = params.containsKey(FILTERS_PARAM) ? params.get(FILTERS_PARAM)
				: DEFAULT_SAVE_FILTER;
//		try {
//			content = applyFilters(content, filters);
//		} catch (Exception e) {
//			problem += "Problem when applying filters " + e.getMessage();
//			log.error(problem);
//		}


		if (problem == null) {
			try {
				String code = SPContentFetchModule.fetchCode(fullPath).getLeft().get();
				String jsx = DaggerSPCellSlotInjectorComponent
						.builder()
						.withContent(content)
						.andCode(code)
						.filters(filters)
						.build()
						.code()
						.get();
				URI fileURI = DaggerURIComponent.builder().from(fullPath).build().uri().get();
				DaggerSaverComponent
						.builder()
						.toURI(fileURI)
						.content(jsx)
						.build()
						.saver()
						.get()
						.save();
			} catch (Exception e) {
				problem = "Problem when saving content to " + fullPath + ":" + e.getMessage();
				log.error(problem);
			}
		}

		String result = problem == null ? "OK" : "KO";
		long now = System.currentTimeMillis();
		long duration = before - now;

		return DaggerOperationResultComponent
				.builder()
				.result(result)
				.operation("FileSaver")
				.target(path)
				.operationTime(duration)
				.problem(problem)
				.build()
				.result();

	};

}


private static String applyFilters(	String content,
									String filters)
		throws Exception {

	String out = content;

	Optional<String> f = Optional.ofNullable(filters);
	if (f.isPresent()) {
		Filter<String, String> fun = DaggerFilterComponent
				.builder()
				.filters(f.get())
				.build()
				.stringToString()
				.get();
		out = fun.apply(content);
	}

	return out;

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
