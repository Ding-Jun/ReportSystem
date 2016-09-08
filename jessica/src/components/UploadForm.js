import React from 'react';
import _ from 'lodash'
import 'whatwg-fetch';
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
  reportExists(rule, value, callback) {
    if (!value) {
      callback();
    } else {
      setTimeout(() => {
        if (value === 'JasonWood') {
          callback([new Error('抱歉，该名称已存在。')]);
        } else {
          callback();
        }
      }, 800);
    }
  }
  handleUploa() {
    console.log('heiehei')
  }
  handleChange() {
    this.doValidateFile();

  }
  handleSubmit(e) {
    console.log("nextStep function", this.props.nextStep)
    this.props.nextStep()
      /*setTimeout(() => {
        this.props.nextStep
      }, 2000)*/
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((errors, values) => {
      if (!!errors) {
        Messager.error('发现错误，请修改！')
        console.log('Errors in form!!!');
        return;
      }
      Messager.info('提交。。。')
        //console.log(values);
      console.log(this.props.form.getFieldsValue());
      console.log(JSON.stringify(this.props.form.getFieldsValue()));
      /*
            fetch('/users', {
              method: 'POST',
              body: JSON.stringify(this.props.form.getFieldsValue())
            }).then(function(response) {
              return response.json();
            }).then(function(data) {
              console.log(data);
            }).catch(function(e) {
              console.log("Oops, error");
            });*/


    });
  }
  handleReturn() {
    history.back(-1)
  }
  doValidateFile(rule, value, callback) {
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
    const {
      getFieldProps,
      getFieldError,
      isFieldValidating
    } = this.props.form;
    const nameProps = getFieldProps('reportName', {
      //ref: "fileaa",
      rules: [{
        required: true,
        min: 5,
        message: '报告名称至少为 5 个字符'
      }, {
        validator: this.reportExists
      }]
    });
    const fileProps = getFieldProps('dataFiles', {
      //ref: "fileaa",
      rules: [{
        required: true,
        min: 1,
        message: '报告wenjian 至少为 5 个字符'
      }, {
        validator: this.doValidateFile.bind(this)
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
      <Form horizontal>
        <FormItem ref="fileItem" label="数据文件" required {...formItemLayout} help={isFieldValidating('dataFiles') ? '校验中...' : (getFieldError('dataFiles') || []).join(', ')} hasFeedback >
          <Input {...fileProps} name="dataFiles" type="file" ref="dataFile" multiple="multiple" accept=".csv" /><Button type="ghost" size="default" ><Icon type="file" />选择</Button>
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
          <Button type="ghost" onClick={this.handleReturn}>返回</Button>
        </FormItem>
      </Form>
    )
  }
}
Uploader = createForm()(Uploader);

export default Uploader;