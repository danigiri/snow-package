package cat.calidos.snowpackage.control.injection;

import java.util.function.BiFunction;

import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import dagger.producers.Produces;
import dagger.producers.ProducerModule;

import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.snowpackage.model.SPCellSlotParserComponent;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SPFiltersModule {

protected final static Logger log = LoggerFactory.getLogger(SPFiltersModule.class);

private static final String _GET = "GET";


@Produces @IntoMap @Named("PostFilters")
@IntKey(1)
public static BiFunction<HttpServletRequest, HttpServletResponse, Boolean> fileToDocument() {

	return (request, response) -> {
		log.trace("------ Request filter request ({} {}) ------", request.getMethod(), request.getServletPath());

		if (!request.getMethod().equals(_GET)) {
			return true;	// continue filter chain
		}

		// now check if appropriate extensions
		if (request.getRequestURL().toString().endsWith(SPCellSlotParserComponent.JSX)) {
		}
		return true;
	};

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

