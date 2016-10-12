/**
 * Created by admin on 2016/10/12.
 */
import React from 'react'
import {Modal, Form, Input, Button,Checkbox,Row ,Col,Tabs} from 'antd'
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
class LoginForm extends React.Component{
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
    this.state={
      visible: false,
      isLogin:false
    }
  }
  showModal() {
    this.setState({
      visible: true
    });
  }
  handleOk() {
    this.setState({
      ModalText: 'The modal dialog will be closed after two seconds',
      confirmLoading: true
    });
    setTimeout(() => {
      this.setState({
        visible: false,
        confirmLoading: false
      });
    }, 2000);
  }
  handleCancel() {
    console.log('Clicked cancel button');
    this.setState({
      visible: false
    });
  }
    render(){
      const text=this.props.text||'login';
      var view;
      if(this.state.isLogin){
       // view=<div onClick={this.showModal.bind(this)}>{text}</div>
      }else{
        view=<div onClick={this.showModal.bind(this)}>{text}</div>
      }
      const style={
        margin:'10px auto'
        //width:'90%'
      }
        return (
         <div>
           {view}
           <Modal title="登录"
                  visible={this.state.visible}
                footer={null}
                  confirmLoading={this.state.confirmLoading}

           >
             <Tabs defaultActiveKey="1" >
               <TabPane tab="login" key="1">
                 <Input style={style} placeholder="Please input the account" /><br/>



                 <Input style={style} type="password" placeholder="Please input the password" /><br/>

                 <Checkbox style={style}>Remember me</Checkbox>


                 <Button style={style}  type="primary" htmlType="submit">Submit</Button>
               </TabPane>
               <TabPane tab="register" key="2">
                 <Input style={style} placeholder="Please input the account" />



                 <Input style={style} type="password" placeholder="Please input the password" />


                 <Button style={style} className="right" type="primary" htmlType="submit">Submit</Button>
               </TabPane>
             </Tabs>





           </Modal>
         </div>
        )
    }
}
LoginForm = Form.create()(LoginForm);
export default LoginForm;
