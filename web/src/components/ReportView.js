import React from 'react';
import { Row, Col,Card } from 'antd';
export default class ReportView extends React.Component {
	render() {
    var layout={
      xs:{
        span:24
      },
      sm:{
        span:16,
        offset:4
      }
    }
		return (
      <Row style={{minWidth:'700px'}}>

				<Col {...layout}>
          <Card title="报告" bordered={false}>
            {this.props.children}
          </Card>
          </Col>



      </Row>
		)
	}
}
