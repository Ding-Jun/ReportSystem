import React from 'react';

export default class ReportView extends React.Component {
	render() {
		return (
			<div>
				<div>ReportView</div>
				{this.props.children}
			</div>
		)
	}
}