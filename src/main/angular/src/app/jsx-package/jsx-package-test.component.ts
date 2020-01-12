// JSX - TEST . COMPONENT . TS

import { AfterViewInit, Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { File, Node, isIdentifier } from '@babel/types';
import { parse, ParserPlugin } from '@babel/parser';
import { traverse, NodePath } from '@babel/core';

import { JSXPackage } from './jsx-package.class';

@Component({
	selector: 'jsx-test',
	template: `
		<form #form_="ngForm">
			<textarea id="input" [(ngModel)]="input" name="name"></textarea>
			<button id="run" (click)="run()">RUN</button>
		</form>
		<div *ngIf="outcome" id="output">{{outcome}}</div>
		<div *ngIf="error" id="error">{{error}}</div>
	`
})

export class JSXPackageTestComponent implements AfterViewInit {

ast: File;
input: string = '';
outcome: string;
error: string;


constructor() {}


ngAfterViewInit() {}


run() {

/*	const code = 	`function formatName(user) {
  return user.firstName + ' ' + user.lastName;
}

const user = {
  firstName: 'Harper',
  lastName: 'Perez',
};

const element = 
      <>
         <h1>Hello, {formatName(user)}!</h1>
          <span>how are <b>you</b> doing?</span>
      </>;

ReactDOM.render(element, document.getElementById('root'));
`;*/

	const jsxp = new JSXPackage(this.input);
	this.outcome = jsxp.extract();

}


}

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
