import React from 'react';
import UploadForm from './UploadForm'
import UploadProcess from './UploadProcess'
import {
  Steps,
  Row,
  Col,
  Card,
  Icon

} from 'antd';
const Step = Steps.Step;
export default class UploadView extends React.Component {
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
    this.state = {
      current: 0

    }
  }

  nextStep() {
    this.setState({
      current: this.state.current + 1
    })
  }
  preStep() {
    this.setState({
      current: this.state.current - 1
    })
  }
  render() {
    var step;
    switch (this.state.current) {
      case 0:
        step = <UploadForm nextStep={this.nextStep.bind(this)}/>;
        break;
      case 1:
        step = <UploadProcess nextStep={this.nextStep.bind(this)} preStep={this.preStep.bind(this)}/>;
        break;
      default:
        step = <UploadForm nextStep={this.nextStep.bind(this)}/>;
    }
    return (
      <div style={{width:"70%",margin:"0 auto"}}>
        <Card title="上传" bordered={false} >
          <Row >
            <Col span={12} offset={6}>
      				<Steps style={{margin:"0 0px 50px"}} current={this.state.current} status="process">
      				  <Step title="选择数据"  />
      				  <Step title="处理"  />
      					<Step title="报告"  />
      				</Steps>
            </Col>
          </Row>
          <Row>
            <Col span={18} offset={4}>
  				  {step}
            </Col>
          </Row>
        </Card>
			</div>
    )
  }
}