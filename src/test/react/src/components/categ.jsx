import React from 'react';

export function Categ(props) {

	if (!props.value0x && !props.value0y && !props.value1x && !props.value1y) {
		return <li className="list-group-item list-group-item-warning">Add some values</li>;
	}

	const value0x = props.value0x ? <li className="list-group-item list-group-item-primary">{props.value0x}</li> : '';
	const value1x = props.value1x ? <li className="list-group-item list-group-item-primary">{props.value1x}</li> : '';
	const value0y = props.value0y ? <li className="list-group-item list-group-item-secondary">{props.value0y}</li> : '';
	const value1y = props.value1y ? <li className="list-group-item list-group-item-secondary">{props.value1y}</li> : '';


	return <ul className="list-group">
				{value0x}
				{value1x}
				{value0y}
				{value1y}
			</ul>;

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