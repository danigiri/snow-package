package cat.calidos.snowpackage.model.injection;

import javax.annotation.Nullable;

import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.BindsInstance;
import dagger.Component;

import cat.calidos.snowpackage.model.SPCellSlot;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=SPCellSlotModule.class)
public interface SPCellSlotComponent {

SPCellSlot slot();

@Component.Builder
interface Builder {

	@BindsInstance Builder with(String code);
	@BindsInstance Builder node(@Nullable Node xmlNode);
	@BindsInstance Builder json(@Nullable JsonNode jsonNode);

	SPCellSlotComponent build();

}

}

/*
 *    Copyright 2024 Daniel Giribet
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

