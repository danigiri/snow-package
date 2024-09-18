// PREVIEW . JSX

import { useLocation, useParams} from 'react-router-dom';

import { Stuff } from './components/stuff';
import { Data } from './components/data';
import { Data3 } from './components/data3';
import { Data4 } from './components/data4';
import { Keyvalue } from './components/keyvalue';
import { Readonly } from './components/readonly';
import { Categ } from './components/categ';

export function Preview() {

	const { component } = useParams();
	const query = useQuery(); 
	let params = {};
	query.forEach( (v, k) => params[k] = v);
	const content = { children: query.has('_VALUE') ? query.get('_VALUE') : '' };

	let preview;
	switch(component) {
		case 'Stuff':
			preview = new Stuff(content);
		break;
		case 'Data':
			preview = new Data(params);
		break;
		case 'Data3':
			preview = new Data3(params);
		break;
		case 'Data4':
			preview = new Data4(params);
		break;
		case 'Data5':
			preview = new Data4(params);
		break;
		case 'Keyvalue':
			preview = new Keyvalue(params);
		break;
		case 'Readonly':
			preview = new Readonly(params);
		break;
		case 'Categ':
			preview = new Categ(params);
		break;
	default:
		preview = '<p>Unknown component</p>';
	}

	return preview.render();

}


function useQuery() {
	return new URLSearchParams(useLocation().search);
}

/*
 *	  Copyright 2024 Daniel Giribet
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

