import React from 'react'
import { findDOMNode } from 'react-dom'
import echarts from 'echarts/lib/echarts'
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/title'
import ChartUtils from '../utils/ChartUtils'
import {Button} from 'antd'
class Chart extends React.Component{
  constructor(props){
    super(props);
    this.state={
      charts:[]
    }
  }
  componentDidMount(){
    this.drawChart();

  }
  componentDidUpdate(){
    //给自己挖个坑
  }
  drawChart(){
    var option = ChartUtils.getBarChartOption(this.props);
    var element= findDOMNode(this.refs.chart);
    var chartInstance = ChartUtils.createChart(element,option);
    console.log("in drawChart:",chartInstance);
    this.props.setChartInstance("chart"+this.props.id,chartInstance);
    /*var charts=this.state.charts;
    charts=chartInstance;
    console.log("in drawChart Charts:",charts);
    this.setState({
      charts:charts
    })*/
  }
  test(){
    console.log(findDOMNode(this.refs.chart))
    //ChartUtils.test()
  }
	render(){
		return (
      <div>

      <div ref="chart" style={{width: "600px",height:"400px",margin:"30px auto"}} >

      </div>
      </div>
    )
	}

}

export default Chart
