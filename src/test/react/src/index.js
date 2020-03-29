import React from 'react';
import ReactDOM from 'react-dom';

import { HashRouter as Router, Switch, Route} from 'react-router-dom';

import { Preview } from './preview';

import 'bootstrap/dist/css/bootstrap.min.css';

import './index.css';

import { Root } from './root';
//const Row = React.lazy(() => import('./components/row'));

// ========================================

const _render = <Router>
					<Switch>
						<Route path="/preview/:component" children={<Preview />} />
						<Route path="/">
							<div className="container-fluid">
								<Root/>
							</div>
						</Route>
					</Switch>
				</Router>;

ReactDOM.render(_render, document.getElementById('root'));

