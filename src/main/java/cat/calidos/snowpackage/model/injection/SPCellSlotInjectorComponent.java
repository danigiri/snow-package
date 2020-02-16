package cat.calidos.snowpackage.model.injection;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.injection.DocumentBuilderModule;
import cat.calidos.morfeu.model.injection.StringToParsedModule;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;

/** From a content file we 
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = {SPCellSlotInjectorModule.class, StringToParsedModule.class, 
								DocumentBuilderModule.class, ListeningExecutorServiceModule.class})
public interface SPCellSlotInjectorComponent {

ListenableFuture<String> code();

@ProductionComponent.Builder
interface Builder {

@BindsInstance Builder withContent(@Named("Content") String content);
@BindsInstance Builder andCode(@Named("Code") String code);

SPCellSlotInjectorComponent build();

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

