import React from 'react'
import { findDOMNode } from 'react-dom'
import _ from 'lodash'
import $ from 'jquery'
import {Button, Card, Row, Col,Table,Spin  } from 'antd'
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
      loading:true,
      report:{
        reportName:"加载中...",
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
              report:rm.data,
              loading:false
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
        <Card title="基本信息" bordered={false}
        style={{lineHeight:"250%"}}>
          <Row>
            <Col {...layout}>
              <p>良率: {report.passPercent}</p>
              <p>测试工程师: {report.testMan}</p>
            </Col>
            <Col {...layout} >
              <p>总数: {report.testCount}</p>
              <p>时间: {report.time}</p>
            </Col>
            <Col>
              <p>原始数据文件: {report.srcFile}</p>
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
      title: '测试序号',
      dataIndex: 'testNo'

    },{
      title: '测试项',
      dataIndex: 'columnName'
    },{
      title: '失效数量',
      dataIndex: 'failCount'
    },{
      title: '失效率(%)',
      dataIndex: 'failRate'
    }];
    const osPreview={
      testNo:0,
      columnName:"OPENSHUT",
      failCount:report.osFailCount,
      failRate:report.osFailRate+"%"
    }
    const RANK_LOW=0;
    var data = _.union([osPreview],report.reportItems);
    data = _.forEach(data,function(reportItem){

      if(reportItem.rank==RANK_LOW){
        reportItem.failRate=<span className="red-text">{reportItem.failRate}</span>;
        reportItem.failCount=<span className="red-text">{reportItem.failCount}</span>;
      }
      return reportItem;
    })

    return (
        <Card title="测试项预览" bordered={false} >
          <Table ref="table" rowKey="id" columns={columns} dataSource={data} pagination={false}/>
        </Card>
      )
  }
  doShowReportDetail(report){
    if(! report.reportItems){
      return null;
    }
    var reportItemList=report.reportItems.map(reportItem=>(
        <Card key={reportItem.id} title={<span>{reportItem.columnName}<a href="#" disabled> {"#"+reportItem.testNo}</a></span>} bordered={false} >

          {reportItem.passChart?<Chart {...reportItem.passChart}/>:null}
          {reportItem.failChart?<Chart {...reportItem.failChart}/>:null}
        </Card>
      ));
    return (
      <Card title="测试项详情" bordered={false} >
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
        <Spin spinning={this.state.loading}>
        {head}
        {preview}
        {detail}
        </Spin>
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
			<div >
        {report}
      </div>
		)
	}
}

export default Report;
