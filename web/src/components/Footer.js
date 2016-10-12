/**
 * Created by admin on 2016/10/10.
 */
import React from 'react'
import {Row,Col} from 'antd'
import {Link} from 'react-router'
class Footer extends React.Component {
  render() {
    var layout={
      xs:{
        span:24
      },
      sm:{
        span:16,
        offset:4
      }
    }
    var itemLayout={
      xs:{
        span:24
      },
      sm:{
        span:12
      }
    }
    return (
      <Row className='white' style={{lineHeight:'250%',margin:'20px 30px'}}>
        <Col {...layout}>

          <Col {...itemLayout}>
            <h3>FUNTEST 数据分析</h3>
            <p>帮助你分析 IC 测试数据，节省时间</p>
            </Col>
          <Col {...itemLayout} >
            <p>BUG反馈：<a href='mailto:dj_dmtsai@163.com?subject=BUG%20Feedback' >dj_dmtsai@163.com</a></p>
            <p>Powered by <a href='#' disabled>FUNTEST</a></p>
            <p>当前版本：<Link to='/document/changelog'>1.3.0</Link></p>
          </Col>

          </Col>

      </Row>
    )
  }
}
export default Footer;
