import React from 'react';
import { Link } from 'react-router'
import { Card, Row, Col } from 'antd';
var RANK_LOW=0;
var RANK_MEDIUM=3;
var RANK_HIGH=5;
class SimpleReport extends React.Component{
  handleClick(e){
    e.preventDefault();
    console.log(this.props.id)
  }
	render(){
    var rankCls;
    switch (this.props.rank){
      case RANK_LOW:rankCls="red lighten-5";break;
      case RANK_MEDIUM:rankCls="orange lighten-5";break;
      case RANK_HIGH:rankCls="light-green lighten-5";break;
      default:;
    }
    var layout={
      xs:24,
      sm:12
    }
		return (
			<Card key={this.props.id}
            className={rankCls}
            title={this.props.reportName}
            bordered={true}
            extra={<Link to={"/report/"+this.props.id}>更多</Link>}
      >

        <Row>
          <Col {...layout}>
            <p>良率: {this.props.passPercent}</p>
            <p>测试工程师: {this.props.testMan}</p>
          </Col>
		      <Col {...layout} >
            <p>测试数量: {this.props.testCount}</p>
            <p>时间: {this.props.time}</p>
          </Col>
        </Row>
		  </Card>
    )

	}
}

export default SimpleReport;
