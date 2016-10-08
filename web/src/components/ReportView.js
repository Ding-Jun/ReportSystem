import React from 'react';
import { Row, Col } from 'antd';
export default class ReportView extends React.Component {
	render() {
    var layout={
      xs:{
        span:24
      },
      sm:{
        span:14,
        offset:5
      }
    }
		return (
      <Row>
			<div>
				<Col {...layout}><h3>报告</h3>
          </Col>
        <Col {...layout}>
          {this.props.children}
        </Col>

			</div>
      </Row>
		)
	}
}
