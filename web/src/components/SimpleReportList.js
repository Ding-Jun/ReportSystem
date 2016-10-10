import React from 'react';
import SimpleReport from './SimpleReport'
import $ from 'jquery'
import {Pagination } from 'antd'
class SimpleReportList extends React.Component {
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here

    this.state = {
      page:{
        curPage:1,
        pageSize:5,
        rowData:[],
        totalPage:5,
        totalRows:0
      }
    }
    this.queryPage(1);
  }
  queryPage(curPage){
    $.ajax({
      type: 'GET',
      url:'/analysis/rs/report/queryPage/'+curPage+"?size=5",
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
  handlePageChange(targetPage){
    console.log("page:",targetPage);
    this.queryPage(targetPage);
  }

  test(){
    this.queryPage(1)

  }
	render(){

    console.log("totalRows:",this.state.page.totalRows);
    var list=this.state.page.rowData.map((simpleReport)=>(
        <div key={simpleReport.id} style={{margin:"10px 0"}}><SimpleReport  {...simpleReport}/></div>
      ))
		return (
	     <div className="SimpleReportList" style={{padding:"10px 30px 50px"}}>
         {/*<Button onClick={this.test.bind(this)} type="primary">defa</Button>*/}
       {list}
         <Pagination className="right"

                     onChange={this.handlePageChange.bind(this)}
                     defaultCurrent={this.state.curPage}
                     pageSize={this.state.page.pageSize}
                     total={this.state.page.totalRows}/>

       </div>
		)
	}
}

export default SimpleReportList
