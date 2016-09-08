import React from 'react';
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
class UploadProcess extends React.Component {
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
    this.state = {

    }
  }
  handleSubmit() {
    console.log('收到表单值：', this.props.form.getFieldsValue());
  }
  handleReturn() {
    //console.log(this.props)
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

    return (
      <Form horizontal>
        <FormItem  {...formItemLayout} label="数据文件">
          <p className="ant-form-text" id="userName" name="userName">大眼萌 minion</p>
        </FormItem>

        <FormItem wrapperCol={{ span: 6 ,offset: 6}}>
          <Button type="primary" onClick={this.handleSubmit.bind(this)}>确定</Button>
          &nbsp;&nbsp;&nbsp;
          <Button type="ghost" onClick={this.handleReturn.bind(this)}>上一步</Button>
        </FormItem>
      </Form>
    )
  }
}

UploadProcess = createForm()(UploadProcess);
export default UploadProcess;