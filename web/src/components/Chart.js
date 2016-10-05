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
    
  }
  componentDidMount(){
    var option = ChartUtils.getBarChartOption(this.props);
    var element= findDOMNode(this.refs.chart);
    ChartUtils.createChart(element,option);
  }
  componentDidUpdate(){
    var option = ChartUtils.getBarChartOption(this.props);
    var element= findDOMNode(this.refs.chart);
    ChartUtils.createChart(element,option);
  }
  test(){
    console.log(findDOMNode(this.refs.chart))
    //ChartUtils.test()
  }
	render(){
		return (
      <div>
      <Button onClick={this.test.bind(this)} type="primary">defa</Button>
      chartType:{this.props.chartType}
      <div ref="chart" style={{width: "600px",height:"400px"}} >
      
      </div>
      </div>
    )
	}

}

export default Chart