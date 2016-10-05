import React from 'react';
import SimpleReport from './SimpleReport'
import $ from 'jquery'
import {Button} from 'antd'
class SimpleReportList extends React.Component {
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
    
    this.state = {
      page:{
        rowData:[]
      }
    }
    this.queryPage(1);
  }
  queryPage(curPage){
    $.ajax({
      type: 'GET',
      url:'/analysis/rs/report/queryPage/'+curPage,
      success:function(rm){
        if(rm.code==1){
          console.log("debug",rm.data);
          this.setState({
            page:rm.data
          })
        }
      }.bind(this)
    })
  }
  test(){
    this.queryPage(1)
    
  }
	render(){
    

    var list=this.state.page.rowData.map((simpleReport)=>(
        <SimpleReport key={simpleReport.id} {...simpleReport}/>
      ))
		return (
	     <div className="SimpleReportList">
       <Button onClick={this.test.bind(this)} type="primary">defa</Button>
       {list}
       </div>
		)
	}
}

export default SimpleReportList