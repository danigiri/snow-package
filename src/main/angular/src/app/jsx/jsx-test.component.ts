// JSX - TEST . COMPONENT . TS

import { Component, Inject, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { File, Node, isIdentifier } from '@babel/types';
import { parse, ParserPlugin } from '@babel/parser';
import { traverse, NodePath } from '@babel/core';

@Component({
	selector: 'jsx-test',
	template: `{{ast}}`
})

export class JSXTestComponent {

ast: File;

constructor() {

	const code = 	`function formatName(user) {
  return user.firstName + ' ' + user.lastName;
}

const user = {
  firstName: 'Harper',
  lastName: 'Perez',
};

const element = 
      <>
         <h1>Hello, {formatName(user)}!</h1>
          <h2>how are you doing?</h2>
      </>;

ReactDOM.render(element, document.getElementById('root'));
`;
	// we ignore this as the ParserPlugin type is closed and does not list plugins
	// @ts-ignore
	const _plugins: ParserPlugin[] = ['jsx', '@babel/plugin-transform-react-jsx-source'];

	this.ast = parse(code, {plugins: _plugins});
	console.debug(this.ast);

	// please see https://github.com/just-jeb/angular-builders/tree/master/packages/custom-webpack
	// https://netbasal.com/customize-webpack-configuration-in-your-angular-application-d09683f6bd22
	// and https://github.com/webpack-contrib/css-loader/issues/447
	// to explain what needs to be done to make traverse actually work, namely modifying the build to redefine
	// an object called 'fs'
	traverse(this.ast, {
		enter(path: NodePath) {	// not sure if this is the right type
			const node: Node = path.node;
			if (isIdentifier(path.node)) {
				console.debug(path.node);
			}

		}
	});

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
