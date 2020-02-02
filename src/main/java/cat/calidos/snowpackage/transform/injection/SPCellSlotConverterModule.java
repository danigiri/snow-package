package cat.calidos.snowpackage.transform.injection;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.transform.Converter;
import cat.calidos.morfeu.transform.StackContext;
import dagger.Module;
import dagger.Provides;

/** injects the code into inputcell slot json nodes 
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SPCellSlotConverterModule {


@Provides
String xml(Converter<JsonNode, String> converter) {
	return converter.process();
}


@Provides
Converter<JsonNode, String> converter(@Named("PopulatedContext") StackContext<JsonNode> context) {
	return new Converter<JsonNode, String>(context);
}


@Provides @Named("PopulatedContext")
StackContext<JsonNode> populatedContext(StackContext<JsonNode> context, JsonNode json) {

	return context;

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

