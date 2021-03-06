import React from 'react';
//import Messager from './Messager'
import {
  Form,
  Button,
  Table
} from 'antd'
import _ from 'lodash'
import $ from 'jquery'
const FormItem = Form.Item;
const createForm = Form.create;
class UploadProcess extends React.Component {
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
  }
  handleSubmit() {
    var dirty=this.refs.table.selectionDirty;
    var dataInfo=this.props.dataInfo;
    var selected=this.refs.table.selectedRowKeys;

    if(dirty){
      if(selected);
    }
    $.ajax({
      type: 'POST',
      url:'/analysis/rs/report/buildReport',
      data:{
        dataInfo:JSON.stringify(dataInfo)
      } ,
     // processData: false,  // 告诉jQuery不要去处理发送的数据
      //contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
      success:function(rm){
        if(rm.code==1 ){
          this.props.nextStep();
          this.props.setResult(rm.data);
        }
        console.log('result',rm.data)
      }.bind(this)

    });
    console.log('收到表单值：', dataInfo);
  }
  handleReturn() {

  //var fileNames=_.pluck(_.where(users, { 'age': 36, 'active': false }), 'user');
   /* const data=_.map(this.props.dataInfo.columns,(columnInfo,i)=>{
      return {
        id:i,
        columnName:columnInfo.columnName,
        limitMin:columnInfo.limitMax
      }

    });

     console.log(this.props.dataInfo.files,data)*/
    this.props.preStep()
  }

  render() {
    const formItemLayout = {
      labelCol: {
        span: 6
      },
      wrapperCol: {
        span: 14
      }
    };
    var fileNames=_.join(_.map(this.props.dataInfo.files,'fileName'),' , ')
    //var fileNames=_.pluck(_.where(this.props.files, { 'status': 36}), 'fileName');
    console.log(this.props.files,fileNames)
    var mode='normal';
    if(this.props.dataInfo.mode ==1){
      mode='FT&RT';
    }
    const columns = [{
      title: '列',
      dataIndex: 'id'
    }, {
      title: '测试项',
      dataIndex: 'columnName'
    }, {
      title: '判限',
      dataIndex: 'limit'
    }, {
      title: '失效数量',
      dataIndex: 'totalCountOutOfLimit'
    }, {
      title: '平均值(Pass)',
      dataIndex: 'realAverageInLimit'
    }, {
      title: '平均值(All)',
      dataIndex: 'realAverageAll'
    }];
    const data=_.map(this.props.dataInfo.columns,(columnInfo)=>{
      var min=(columnInfo.limitMin==-Number.MAX_VALUE)?'':columnInfo.limitMin;
      var max=(columnInfo.limitMax==Number.MAX_VALUE)?'':columnInfo.limitMax;
      return {
        id:columnInfo.id,
        columnName:columnInfo.columnName,
        limit:'('+min+','+max+')'+columnInfo.limitUnit,
        passGroups:columnInfo.passGroups,
        failGroups:columnInfo.failGroups,
        totalCountOutOfLimit:columnInfo.totalCountOutOfLimit,
        totalCountAll:columnInfo.totalCountAll,
        realAverageAll:columnInfo.realAverage.toFixed(2),
        realAverageInLimit:columnInfo.realAverageInLimit.toFixed(2),
        isProcess:columnInfo.isProcess
      }

    });
    var $this=this;
    const rowSelection = {
      getCheckboxProps: record => {
        return{
           defaultChecked:record.isProcess,    // 配置无法勾选的列
           disabled:record.totalCountAll==0
        }

      },
      onChange:function onChange(selectedRowKeys, selectedRows) {
        console.log('dthis',$this.refs.table)
        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      },
      onSelect(record, selected, selectedRows) {

        console.log(record, selected, selectedRows);
      },
      onSelectAll(selected, selectedRows, changeRows) {
        console.log(selected, selectedRows, changeRows);
      }
    };
    //const data =this.props.dataInfo.columns;

    var status=1111;
    var fileInfos=_.map(this.props.dataInfo.files,(fileInfo,i) =>(
      <li key={i}><span>{fileInfo.fileName+' : '}</span>{((status==fileInfo.status)?<span className='blue-text text-lighten-1'>可处理</span>:<span className='red-text'>{fileInfo.message}</span>)}</li>
    ))
    return (
      <Form horizontal>
        <FormItem  {...formItemLayout} label='数据文件'>
          <p className='ant-form-text '  name='userName'>{fileNames}</p>
        </FormItem>
        <FormItem  {...formItemLayout} label='报告名称'>
          <p className='ant-form-text '  name='userName'>{this.props.dataInfo.reportName}</p>
        </FormItem>
        <FormItem  {...formItemLayout} label='芯片名称'>
          <p className='ant-form-text '  name='userName'>{this.props.dataInfo.chipName}</p>
        </FormItem>
        <FormItem  {...formItemLayout} label='处理模式'>
          <p className='ant-form-text '  name='userName'>{mode}</p>
        </FormItem>
        <FormItem>
          <h3 className='blue-text text-lighten-1 '>详情</h3>
            <hr/>
            <ul>
              {fileInfos}
            </ul>

        </FormItem>

        <FormItem >
          <h3 className='blue-text text-lighten-1'>请勾选需要图表展示的测试项(以debug结尾的测试项默认不勾选)</h3>
          <hr/><br/>
            <Table ref='table' rowKey='id' rowSelection={rowSelection} columns={columns} dataSource={data} pagination={false}/>

        </FormItem>
        <FormItem wrapperCol={{ span: 6 ,offset: 6}}>
          <Button type='primary' onClick={this.handleSubmit.bind(this)} disabled={this.props.dataInfo.columns==null ||this.props.dataInfo.columns.length==0}>确定</Button>
          &nbsp;&nbsp;&nbsp;
          <Button type='ghost' onClick={this.handleReturn.bind(this)}>上一步</Button>
        </FormItem>
      </Form>
    )
  }
}

UploadProcess = createForm()(UploadProcess);
export default UploadProcess;
