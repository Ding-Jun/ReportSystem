import React from 'react';
import {
  findDOMNode
} from 'react-dom'
import _ from 'lodash'
import $ from 'jquery'
import Messager from './Messager'
import {
  Input,
  Form,
  Button,
  Icon,
  Upload,
  Checkbox,
  Select,
  Tooltip
} from 'antd'

const FormItem = Form.Item;
const createForm = Form.create;
const Option = Select.Option;
var options = ['JW1121', 'JW7715', 'JW7705'].map((domain) => {

  return <Option key={domain}>{domain}</Option>;
});

class Uploader extends React.Component {
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((errors, values) => {
      if (!!errors) {
        Messager.error('发现错误，请修改！')
        console.log('Errors in form!!!');
        return;
      }
      Messager.info('提交。。。')
      //var form=findDOMNode(this.refs.form);
      var form=$("#form")[0];
       console.log("$form:",form);
      var formData= new FormData(form);
      formData.append('username', 'Chris');
      console.log("formdata:",formData)
      $.ajax({
        type: 'POST',
        url:'/analysis/rs/report/buildDataInfo',
        data:formData ,
        processData: false,  // 告诉jQuery不要去处理发送的数据
        contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
        success:function(rm){
          if(rm.code==1 ){
            this.props.nextStep();
            this.props.setDataInfo(rm.data);
          }
          console.log("result",rm.data)
        }.bind(this)
        //console.log(this.props.form.getFieldsValue());
        //console.log(JSON.stringify(this.props.form.getFieldsValue()));

      });
    });
  }
  handleReturn() {
  }
  validateReportName(rule, value, callback) {
    if (!value) {
      callback();
    } else {
      if(value.length>=5){
         $.ajax({
          type: "GET",
          url:'/analysis/rs/report/isExists',
          data:{
            reportName:value
          },
          success:function(rm){
            if(rm.data==true){
              callback([new Error('isExists')]);
            }
            else{
              callback();
            }
          }
        });
      }else{
        callback([new Error('报告名称至少为 5 个字符')]);
      }
    }
  }
  validateFile(rule, value, callback) {
    var isCompleted = false;
    //console.log('changed')
    if (!value) {
      callback();
      isCompleted = true;
    } else {

      console.log("file", this.refs.dataFile.refs.input.files)
      var fileList = this.refs.dataFile.refs.input.files;

      for (let i = 0; i < fileList.length; i++) {
        var file = fileList[i];
        var rs = /(.csv)$/i.test(file.name);
        if (!rs) {
          callback([new Error('抱歉，目前仅支持csv格式数据。')]);
          isCompleted = true;
          break;
        }
        if (file.size > 50 * 1024 * 1024) {
          callback([new Error('抱歉，' + file.name + ' 太大,超过了限制')]);
          isCompleted = true;
          break;
        }
      }
      if (!isCompleted) {
        callback()
      }
    }

  }
  render() {
    console.log("render:");
    const {
      getFieldProps,
      getFieldError,
      isFieldValidating
    } = this.props.form;
    const nameProps = getFieldProps('reportName', {
      //ref: "fileaa",
      rules: [{
        required: true,
        min: 1,
        message: '报告名称至少为 5 个字符'
      },{
        validator: this.validateReportName
      }]
    });
    const fileProps = getFieldProps('dataFiles', {
      //ref: "fileaa",
      rules: [{
        required: true,
        min: 1,
        message: '报告wenjian 至少为 5 个字符'
      }, {
        validator: this.validateFile.bind(this)
      }]
    });
    const chipNameProps = getFieldProps('chipName', {
      initialValue: "undefined"
    });
    const modeProps = getFieldProps('mode', {
      initialValue: false
    });
    const formItemLayout = {
      labelCol: {
        span: 6
      },
      wrapperCol: {
        span: 6
      }
    };
    return (
      <Form ref="form" id="form" horizontal encType="multipart/form-data">
        <FormItem ref="fileItem" label="数据文件" required {...formItemLayout} help={isFieldValidating('dataFiles') ? '校验中...' : (getFieldError('dataFiles') || []).join(', ')} hasFeedback >
          <Input {...fileProps} name="files" type="file" ref="dataFile" multiple="multiple" accept=".csv" /><Button type="ghost" size="default" ><Icon type="file" />选择</Button>
        </FormItem>
    <FormItem label="报告名称" required {...formItemLayout} help={isFieldValidating('reportName') ? '校验中...' : (getFieldError('reportName') || []).join(', ')} hasFeedback >
          <Input {...nameProps} name="reportName" placeholder="数据处理后产生的报告名称"  />
        </FormItem>
          <FormItem label="芯片名称"   {...formItemLayout} help="">
          <Select combobox
            ref="chipName"
            name="chipName"
            {...chipNameProps}
            filterOption={false}
            placeholder="暂时没用"
          >
            {options}
          </Select>
        </FormItem>
        <FormItem label={<span>其他 <Tooltip title="FT+RT混合提交时   删除FT不良品数据"><Icon type="question-circle-o" /></Tooltip></span>} {...formItemLayout}>
          <Checkbox {...modeProps} name="mode">删除FT不良品数据</Checkbox>
        </FormItem>
        <FormItem wrapperCol={{ span: 6 ,offset: 6}}>
          <Button type="primary" onClick={this.handleSubmit.bind(this)}>下一步</Button>
          &nbsp;&nbsp;&nbsp;
          <Button type="ghost" onClick={this.handleReturn.bind(this)}>返回</Button>
        </FormItem>
      </Form>
    )
  }
}
Uploader = createForm()(Uploader);

export default Uploader;