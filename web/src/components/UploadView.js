import React from 'react';
import UploadForm from './UploadForm'
import UploadProcess from './UploadProcess'
import UploadResult from './UploadResult'
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
      current: 0,
      dataInfo:{},
      result:null
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
  setDataInfo(dataInfo){
    this.setState({
      dataInfo:dataInfo
    })
  }
  setResult(result){
    this.setState({
      result:result
    })
  }
  render() {
    var step;
    switch (this.state.current) {
      case 0:
        step = <UploadForm nextStep={this.nextStep.bind(this)} setDataInfo={this.setDataInfo.bind(this)}/>;
        break;
      case 1:
        step = <UploadProcess nextStep={this.nextStep.bind(this)} dataInfo={this.state.dataInfo}  preStep={this.preStep.bind(this)} setResult={this.setResult.bind(this)}/>;
        break;
      case 2:
        step = <UploadResult result={this.state.result}/>;
        break;
      default:
        step = <UploadForm nextStep={this.nextStep.bind(this)}/>;
    }
    var viewLayout = {
      xs:{
        span:24
      },
      sm:{
        span:16,
        offset:4
      }
    }
    var rcLayout={
      xs:{
        span:24
      },
      sm:{
        span:16,
        offset:4
      }
    }
    return (
      <Row >
        <Col {...viewLayout}>
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
              <Col {...rcLayout}>
                {step}
              </Col>
            </Row>
          </Card>
        </Col>
			</Row>
    )
  }
}
