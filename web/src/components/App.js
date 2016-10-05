require('normalize.css/normalize.css');
//require('styles/App.css');
//import 'antd/dist/antd.css';
import React from 'react';


import Navigation from '../components/Navigation'
let yeomanImage = require('../images/yeoman.png');

class AppComponent extends React.Component {


	render() {
		return (
			<div className="index" >
        <Navigation/>

        <div style={{ background: '#ECECEC', padding: '30px' }}>
        {this.props.children}
        </div>
      </div>
		);
	}
}

AppComponent.defaultProps = {};

export default AppComponent;