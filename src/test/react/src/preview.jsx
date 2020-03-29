//import React from 'react';
//import ReactDOM from 'react-dom';

import { useLocation, useParams} from 'react-router-dom';

import { Stuff } from './components/stuff';
import { Data } from './components/data';
import { Data3 } from './components/data3';
import { Data4 } from './components/data4';
import { Keyvalue } from './components/keyvalue';
import { Readonly } from './components/readonly';
import { Categ } from './components/categ';

export function Preview(props) {

	const { component } = useParams();
	const query = useQuery(); 
	let params = {};
	query.forEach( (v, k) => params[k] = v);
	const content = { children: query.has('_VALUE') ? query.get('_VALUE') : '' };

	let preview;
	switch(component) {
		case 'Stuff':
			preview = Stuff(content);
		break;
		case 'Data':
			preview = Data(params);
		break;
		case 'Data3':
			preview = Data3(params);
		break;
		case 'Data4':
			preview = Data4(params);
		break;
		case 'Data5':
			preview = Data4(params);
		break;
		case 'Keyvalue':
			preview = Keyvalue(params);
		break;
		case 'Readonly':
			preview = Readonly(params);
		break;
		case 'Categ':
			preview = Categ(params);
		break;
	default:
		preview = '';
	}

	return  preview;

}


function useQuery() {
	return new URLSearchParams(useLocation().search);
}

