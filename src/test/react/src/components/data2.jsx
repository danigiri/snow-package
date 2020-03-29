import React from 'react';

export function Data2(props) {
	return 	<div className="card">
				<div className="card-body">
					<div className="card-title">Max two elements: {props.text}</div>
					<p className="card-text">{props.number}</p>
				</div>
			</div>;
}
