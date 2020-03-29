import React from 'react';

export function Stuff(props) {

	if (!props.children || props.children==='$_VALUE') {
		return <div className="alert alert-warning" role="alert">Add content to this Stuff</div>
	}

	return <pre>{props.children}</pre>;

}
