import React from 'react';

export function Data3(props) {

	const color_ = '#'+(props.color ?? 'ff0000');

	return 	<div className="card"> 
				<div className="card-body" style={{backgroundColor: color_}}>
					<h4 className="card-title">{props.text}</h4>
				</div>
			</div>;
}




