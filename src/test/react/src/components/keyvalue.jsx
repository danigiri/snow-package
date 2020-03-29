import React from 'react';

export function Keyvalue(props) {

	let value_ = props.value ?? <span className="badge badge-warning">No value</span>;

	return 	<p>{props.key_}: {value_}</p>;

}
