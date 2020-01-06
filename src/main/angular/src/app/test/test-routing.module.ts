// TEST - ROUTING . MODULE . TS

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { JSXTestComponent } from '../jsx/jsx-test.component';

const routes: Routes = [
						{path: 'jsx-test', component: JSXTestComponent},
//						{path: 'jsx-test/:case_', component: JSXTestComponent},
						//{path: 'presentation-test/:case_', component: PresentationTestComponent}
];

@NgModule({
	imports: [
				CommonModule,
				FormsModule,
				RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	declarations: [
					JSXTestComponent,
//					PresentationTestComponent
	]
})


export class TestRoutingModule { }

/*
 *	  Copyright 2020 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
