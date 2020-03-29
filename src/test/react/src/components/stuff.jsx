import React from 'react';

export function Stuff(props) {

	if (!props.children) {
		return <div className="alert alert-warning" role="alert">Add not empty content to this Stuff</div>
	}

	return <pre>{props.children}</pre>;

}
