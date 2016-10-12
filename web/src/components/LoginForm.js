/**
 * Created by admin on 2016/10/12.
 */
import React from 'react'
import {Modal} from 'antd'
class LoginForm extends React.Component{
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
    this.state={
      visible: false
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
        return (
         <div>
           <div onClick={this.showModal.bind(this)}>{text}</div>
           <Modal title="Title of the modal dialog"
                  visible={this.state.visible}
                  onOk={this.handleOk.bind(this)}
                  confirmLoading={this.state.confirmLoading}
                  onCancel={this.handleCancel.bind(this)}
           >
           </Modal>
         </div>
        )
    }
}
export default LoginForm;
