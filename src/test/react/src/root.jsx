import React from 'react';

/* eslint-disable no-unused-vars */
import { Row } from './components/row';
import { Col } from './components/col';
import { Stuff } from './components/stuff';
import { Data } from './components/data';
import { Data2 } from './components/data2';
import { Data3 } from './components/data3';
import { Data4 } from './components/data4';
import { Data5 } from './components/data5';
import { Keyvalue } from './components/keyvalue';
import { Holderwell } from './components/holderwell';
import { Readonly } from './components/readonly';
import { Categ } from './components/categ';
/* eslint-enable no-unused-vars */

export function Root() {
	const _root = 
<>
	<Row>
		<Col size="4">
			<Stuff>aaa1</Stuff>
			<Data text="foo" number="1"/>
			<Keyvalue key_="key" />
		</Col>
		<Col size="8">
			<Stuff>aaa2</Stuff>
			<Data3 text="foo2" color="aa00ff"/>
			<Data5 text="foox" color="2a20ff"/>
			<Readonly number="1234" />
		</Col>
	</Row>
</>;
	return _root;
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
