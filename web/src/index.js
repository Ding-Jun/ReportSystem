import 'core-js/fn/object/assign';
import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, hashHistory, IndexRoute } from 'react-router'
import App from './components/App';
import HomeView from './components/HomeView';
import UploadView from './components/UploadView';
import ReportView from './components/ReportView';
import SimpleReportList from './components/SimpleReportList'
import Report from './components/Report'
//global vars
var root="/analysis/rs/";
// Render the main component into the dom
ReactDOM.render(
	<Router history={hashHistory}>
    <Route path="/" component={App}>
      <IndexRoute component={HomeView}/>
      <Route path="/upload" component={UploadView}>
        		
      </Route>
      <Route path="/report" component={ReportView}>
      	<Route path="/report/reportList" component={SimpleReportList}/>
        <Route path="/report/:id" component={Report} />
      </Route>

    </Route>
  </Router>
      , document.getElementById('app'));