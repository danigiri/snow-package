package cat.calidos.snowpackage.control;

import java.util.Map;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.snowpackage.control.injection.DaggerSPControlComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SPServlet extends MorfeuServlet {


@Override
public ControlComponent getControl(String path, Map<String, String> params) {
	return DaggerSPControlComponent.builder()
									.withPath(path)
									.method(ControlComponent.GET)
									.withParams(params)
									.andContext(this.context)
									.build();
}


@Override
public ControlComponent postControl(String path, Map<String, String> params) {
	return DaggerSPControlComponent.builder()
									.withPath(path)
									.method(ControlComponent.POST)
									.withParams(params)
									.andContext(this.context)
									.build();
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

