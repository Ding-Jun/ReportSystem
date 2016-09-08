import React from 'react'
import {
  Row,
  Col,
  Card
} from 'antd'

export default class Transaction extends React.Component {
  handleChange(value) {
    console.log(`selected ${value}`);
  }

  render() {
    return (
      <Row type="flex" justify="center">
        <Col xs={24} sm={4} >
          <Card title="Card title" extra={<a href="#">More</a>} style={{ width: 300 }}>
              <p>Card content</p>
          </Card>
        </Col>
        <Col xs={24} sm={4}>
          <Card title="Card title" extra={<a href="#">More</a>} style={{ width: 300 }}>
              <p>Card content</p>
          </Card>
        </Col>
        <Col xs={24} sm={4}>
          <Card title="Card title" extra={<a href="#">More</a>} style={{ width: 300 }}>
              <p>Card content</p>
          </Card>
        </Col>
      </Row>
    )
  }
}