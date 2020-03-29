import React from 'react';

export function Keyvalue(props) {

	const key_ = props.key_ ?? <small>key goes here</small>;
	const value_ = props.value ?? <span className="badge badge-warning">No value</span>;

	return 	<p>{key_}: {value_}</p>;

}
