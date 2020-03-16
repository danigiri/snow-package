import React from 'react';
import ReactDOM from 'react-dom';

import 'bootstrap/dist/css/bootstrap.min.css';

import './index.css';

import { Root } from './root';
//const Row = React.lazy(() => import('./components/row'));

// ========================================


ReactDOM.render(<div className="container-fluid"><Root/></div>, document.getElementById('root'));

