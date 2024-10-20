package cat.calidos.snowpackage.control.injection;

import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;


@ProductionComponent(modules = { SPContentGETControlModule.class, SPContentFetchModule.class,
		ListeningExecutorServiceModule.class })
public interface SPContentGETControlComponent {

String parsedCode();

//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder fromPath(@Named("Path") String path);
	@BindsInstance Builder withPrefix(@Named("Prefix") String prefix);
	@BindsInstance Builder filters(@Named("Filters") String filters);	
	@BindsInstance Builder withProperties(@Nullable Properties config); // will use empty configuration if not set
	@BindsInstance Builder andProblem(@Named("Problem") String problem); // if exception we will return this

	SPContentGETControlComponent build();
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
