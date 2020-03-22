import React from 'react';

import { Data3 } from './data3';

export function Data5(props) {

	const firstPart = Data3(props);

	return 	<>
				<span className="badge badge-info">From POST</span>
				{firstPart}
			</>;

}
