import React from 'react';

export function Data(props) {

	const title = props.text && <div className="card-title">{props.text}</div>;

	return 	<div className="card text-white bg-info">
				<div className="card-body">
					{title}
					<p className="card-text">{props.number}</p>
				</div>
			</div>;

}
