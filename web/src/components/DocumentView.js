/**
 * Created by admin on 2016/10/8.
 */
import React from 'react'
import {Row, Col, Card} from 'antd';
class DocumentView extends React.Component {
  render() {
    var layout = {
      xs: {
        span: 24
      },
      sm: {
        span: 16,
        offset: 4
      }
    }
    return (
      <Row>

        <Col {...layout}>
            {this.props.children}
        </Col>
      </Row>

    )
  }
}

export default DocumentView;
