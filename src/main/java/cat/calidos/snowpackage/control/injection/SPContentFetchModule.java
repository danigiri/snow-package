package cat.calidos.snowpackage.control.injection;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Pair;
import cat.calidos.morfeu.utils.injection.DaggerDataFetcherComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;


@Module
public class SPContentFetchModule {

protected final static Logger log = LoggerFactory.getLogger(SPContentFetchModule.class);

// we return a pair of result and exception, we assume module user components will also be modules
// that will build some sort of error response at all times, we let the other modules in the
// component decide what to do
@Provides @Named("EffectiveCode")
public static Pair<Optional<String>, Optional<Exception>> fetchCode(@Named("FullPath") String fullPath) {

	String code = null;
	Exception exception = null;
	try {
		URI fileURI = DaggerURIComponent.builder().from(fullPath).build().uri().get();
		InputStream content = DaggerDataFetcherComponent
				.builder()
				.forURI(fileURI)
				.build()
				.fetchData()
				.get();
		code = IOUtils.toString(content, Config.DEFAULT_CHARSET);
	} catch (Exception e) {
		log.error("Could not fetch raw code for '{}' ({})", fullPath, e.getMessage());
		exception = e;
	}
	return new Pair<>(Optional.ofNullable(code), Optional.ofNullable(exception));

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
