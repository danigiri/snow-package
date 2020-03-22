import React from 'react';

export function Data2(props) {
	return 	<div class="card">
				<div class="card-body">
				<div class="card-title">Max two elements: {props.text}</div>
				<p class="card-text">{props.number}</p>
				</div>
			</div>;
}
