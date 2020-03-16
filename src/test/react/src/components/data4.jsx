import React from 'react';

import { Data } from './data';

export function Data4(props) {

	if (props.text===undefined && props.number===undefined) {
		return <div className="alert alert-danger" role="alert">Empty</div>
	} else {
		return Data(props);	// if we have some content we are the same as <Data>
	}

}
