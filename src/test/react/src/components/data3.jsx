import React from 'react';

export function Data3(props) {

	const color_ = '#'+(props.color ?? 'ff0000');

	return 	<div className="card"> 
				<div className="card-body" style={{backgroundColor: color_}}>
					<h4 className="card-title">{props.text}</h4>
				</div>
			</div>;

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


