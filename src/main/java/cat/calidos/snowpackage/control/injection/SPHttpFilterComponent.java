package cat.calidos.snowpackage.control.injection;

import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;
import cat.calidos.morfeu.webapp.injection.HttpFilterComponent;
import cat.calidos.morfeu.webapp.injection.HttpFilterModule;
import cat.calidos.morfeu.webapp.injection.POSTFileSaverModule;


/** List modules that will be called on the filter, namely any special code slot wrapping ones and the POST saver
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={HttpFilterModule.class, POSTFileSaverModule.class, ListeningExecutorServiceModule.class})
public interface SPHttpFilterComponent extends HttpFilterComponent {

@ProductionComponent.Builder
interface Builder extends HttpFilterComponent.Builder {}

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

