/**
 * Created by admin on 2016/10/10.
 */
import React from 'react'
import {Card, Timeline,Alert} from 'antd'
class Changelog extends React.Component {
  render() {
    return (
      <Card title="更新日志" bordered={false}>
        <Timeline>
          <Timeline.Item>
            <h2>V1.3.1</h2>
            <p>2016-03-30&nbsp;&nbsp;&nbsp;数据处理逻辑优化</p>
            <p>2016-03-30&nbsp;&nbsp;&nbsp;上传文件时没反应多半是因为没有删除QA data</p>
          </Timeline.Item>
          <Timeline.Item>
            <h2>V1.3.0</h2>
            <p>2016-10-11&nbsp;&nbsp;&nbsp;UI更新</p>
            <p>2016-10-10&nbsp;&nbsp;&nbsp;以颜色区分良率 95%~100%绿色 90%~95%黄色 90%以下红色</p>
            <p>2016-10-09&nbsp;&nbsp;&nbsp;平均值、标准差、CPK计算值校准</p>
            <p>2016-09-26&nbsp;&nbsp;&nbsp;优化数据处理速度</p>
            <p>2016-09-22&nbsp;&nbsp;&nbsp;增加处理过程中的错误提示</p>
            <p>2016-09-19&nbsp;&nbsp;&nbsp;修复不能处理中文名数据的问题</p>
          </Timeline.Item>
          <Timeline.Item>
            <h2>V1.2.0</h2>
            ...
          </Timeline.Item>
          <Timeline.Item>
            <h2>V1.1.0</h2>
            ...
          </Timeline.Item>
          <Timeline.Item>
            <h2>V1.0.0</h2>
            <p>基础功能实现</p>
          </Timeline.Item>
        </Timeline>
        <h2>ToDo List</h2>
        <Alert message="1. 登陆、注册" type="info" />
        <Alert message="2. 使用说明" type="info" />
        <Alert message="3. 首页Banner制作" type="info" />
      </Card>
    )
  }
}
export default Changelog;
