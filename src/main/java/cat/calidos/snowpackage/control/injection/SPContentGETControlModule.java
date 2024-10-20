package cat.calidos.snowpackage.control.injection;

import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;

import cat.calidos.morfeu.utils.Pair;
import cat.calidos.snowpackage.model.injection.DaggerSPCellSlotParserComponent;


@Module
public class SPContentGETControlModule {

protected final static Logger log = LoggerFactory.getLogger(SPContentGETControlModule.class);

@Provides
public static String parsedCode(@Named("Path") String path,
								@Named("Prefix") String prefix,
								@Named("EffectiveCode") Pair<Optional<String>, Optional<Exception>> effectiveCode,
								@Named("Filters") String filters,
								@Nullable Properties config,
								@Named("Problem") String problem) {

	if (effectiveCode.getRight().isPresent()) {
		log.error("Skipped get code slots for [{}]{} ({})", prefix, path, effectiveCode.getRight().get().getMessage());
		return problem;
	}
	String doc;
	try {

		doc = DaggerSPCellSlotParserComponent
				.builder()
				.fromPath(path)
				.withCode(effectiveCode.getLeft().get())
				.filters(filters)
				.withProperties(config)
				.build()
				.content();
		log.trace("Generated document length: {}", doc.length());
		// System.err.println(doc);
	} catch (Exception e) {
		log.error("Could not get code slots for [{}]{} ({})", prefix, path, e.getMessage());
		doc = problem;
	}

	return doc;
};


@Provides @Named("FullPath")
String fullPath(@Named("Path") String path,
				@Named("Prefix") String prefix) {
	return prefix + path;
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
