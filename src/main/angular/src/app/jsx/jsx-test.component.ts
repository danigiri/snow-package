// JSX - TEST . COMPONENT . TS

import { Component, Inject, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { File } from '@babel/types';
import { parse, ParserPlugin } from '@babel/parser';

@Component({
	selector: 'jsx-test',
	template: `{{parsed}}`
})

export class JSXTestComponent {

parsed: File;

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

	const _plugins: ParserPlugin[] = ['jsx', '@babel/plugin-transform-react-jsx-source'];
	this.parsed = parse(code, {plugins: _plugins});
	console.debug(this.parsed);

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
