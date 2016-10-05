import React from 'react'
import { findDOMNode } from 'react-dom'
import _ from 'lodash'
import $ from 'jquery'
import {Button, Card, Row, Col,Table  } from 'antd'
import echarts from 'echarts/lib/echarts'
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/title'
import ChartUtils from '../utils/ChartUtils'
import Chart from './Chart'
class Report extends React.Component{
  constructor(props){
    super(props);
    this.state={
      report:{
        reportName:"u",
        time:""
      }
    }
    console.log(this.props.params.id)
    this.queryReport(this.props.params.id);
  }
  componentDidUpdate(){
    if(! this.state.report.reportItems){
      return;
    }
    _.forEach(this.state.report.reportItems,reportItem=>{
      var passchart=reportItem.passChart;
    })
    
  }
  queryReport(id){
    
      $.ajax({
        type: 'GET',
        url:'/analysis/rs/report/queryReport/'+id,
        success:function(rm){
          if(rm.code==1){
            console.log("debug",rm.data);
            this.setState({
              report:rm.data
            })
          }
        }.bind(this)
      })
    
  }
  doShowReportHead(report){
    var layout={
      xs:24,
      sm:12
    }
    return (
        <Card title="Base Info" bordered={false} >
          <Row>
            <Col {...layout}>
              <p>LiangLv: {report.passPercent}</p>
              <p>testMan: {report.testMan}</p>
            </Col>
            <Col {...layout} >
              <p>Count: {report.testCount}</p>
              <p>time: {report.time}</p>
            </Col>
            <Col>
              <p>SRC: {report.srcFile}</p>
            </Col>
          </Row>
        </Card>
      )
  }
  doShowReportPreview(report){
    if(! report.reportItems){
      return null;
    }

    const columns = [{
      title: 'TestNo',
      dataIndex: 'testNo'

    },{
      title: 'Name',
      dataIndex: 'columnName'
    },{
      title: 'Fail Count',
      dataIndex: 'failCount'
    },{
      title: 'Fail Rate(%)',
      dataIndex: 'failRate'
    }];
    const osPreview={
      testNo:0,
      columnName:"OPENSHUT",
      failCount:report.osFailCount,
      failRate:report.osFailRate
    }
    const data = _.union([osPreview],report.reportItems)
    
    return (
        <Card title="TestItem Preview" bordered={false} >
          <Table ref="table" rowKey="id" columns={columns} dataSource={data} pagination={false}/>
        </Card>
      )
  }
  doShowReportDetail(report){
    if(! report.reportItems){
      return null;
    }
    var reportItemList=report.reportItems.map(reportItem=>(
        <Card key={reportItem.id} title={<span>{reportItem.columnName}<a href="#"> {"#"+reportItem.testNo}</a></span>} bordered={false} >
          <p>{"TestNo: "+reportItem.testNo}</p>
          {reportItem.passChart?<Chart {...reportItem.passChart}/>:null}
          {reportItem.failChart?<Chart {...reportItem.failChart}/>:null}
        </Card>
      ));
    return (
      <Card title="TestItem Detail" bordered={false} >
          {reportItemList}
      </Card>
    )
  }


  showReport(report){
    var layout={
      xs:24,
      sm:12
    }
    var head=this.doShowReportHead(report);
    var preview=this.doShowReportPreview(report);
    var detail=this.doShowReportDetail(report);
    return (
      <div>
        <h1>{report.reportName}</h1>
        {head}
        {preview}
        {detail}
      </div>
    )
  }
  test(){
    //this.queryReport(6)
    ChartUtils.test()
  }
	render(){
    var report=this.showReport(this.state.report)
		return (
			<div>
        Report:{this.props.params.id}
        <Button onClick={this.test.bind(this)} type="primary">defa</Button>
        {report}
      </div>
		)
	}
}

export default Report