import React from 'react';
import Transaction from '../components/Transaction'
import Gallery from '../components/Gallery'
export default class HomeView extends React.Component {
	render(){
		return (
		  <div>
        <Gallery/>
        {/*<Transaction/>*/}
      </div>

    )
	}
}
