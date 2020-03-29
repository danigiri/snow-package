import React from 'react';

export function Categ(props) {

	if (!props.value0x && !props.value0y && !props.value1x && !props.value1y) {
		return <li class="list-group-item list-group-item-warning">Add some values</li>;
	}
	
	const value0x = props.value0x ? <li class="list-group-item list-group-item-primary">{props.value0x}</li> : '';
	const value1x = props.value1x ? <li class="list-group-item list-group-item-primary">{props.value1x}</li> : '';
	const value0y = props.value0y ? <li class="list-group-item list-group-item-secondary">{props.value0y}</li> : '';
	const value1y = props.value1y ? <li class="list-group-item list-group-item-secondary">{props.value1y}</li> : '';


	return <ul Class="list-group">
				{value0x}
				{value1x}
				{value0y}
				{value1y}
			</ul>;
}
