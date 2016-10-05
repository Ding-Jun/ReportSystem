import React from 'react';
import { Link } from 'react-router'
import { Card, Row, Col } from 'antd';

class SimpleReport extends React.Component{
  handleClick(e){
    e.preventDefault();
    console.log(this.props.id)
  }
	render(){
    var layout={
      xs:24,
      sm:12
    }
		return (
			<Card title={this.props.reportName} extra={<Link to={"/report/"+this.props.id}>More</Link>}>
        <Row>
          <Col {...layout}>
            <p>LiangLv: {this.props.passPercent}</p>
            <p>testMan: {this.props.testMan}</p>
          </Col>
		      <Col {...layout} >
            <p>Count: {this.props.testCount}</p>
            <p>time: {this.props.time}</p>
          </Col>
        </Row>
		  </Card>
    )
		
	}
}

export default SimpleReport;