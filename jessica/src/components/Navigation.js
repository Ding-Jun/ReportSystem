import React from 'react';
import {
  Link
} from 'react-router'
import {
  Menu,
  Icon
} from 'antd'
export default class Navigation extends React.Component {
  constructor(props) {
    super(props);
    // Operations usually carried out in componentWillMount go here
    this.state = {
      current: 'mail'

    }
  }
  handleClick = (e) => {
    console.log('click ', e);
    this.setState({
      current: e.key
    });
  }

  render() {
    return (
      <Menu onClick={this.handleClick}
        		selectedKeys={[this.state.current]}
          		mode="horizontal" 
          >
          <Menu.Item key="logo">
            <Icon type="aliwangwang" />FUNTEST数据分析
          </Menu.Item>
          
          <Menu.Item key="index">
            <Link to="/"><Icon type="home" />首页</Link>
            
          </Menu.Item>
          <Menu.Item key="upload">
            <Link to="/upload"><Icon type="upload" />上传</Link>
            
          </Menu.Item>
          <Menu.Item key="report">
          <Link to="/report"><Icon type="bar-chart" />报告</Link>
            
          </Menu.Item>
          <Menu.Item key="config">
          <Link to="/config"><Icon type="setting" />配置</Link>
            
          </Menu.Item>
        </Menu>
    )
  }

}