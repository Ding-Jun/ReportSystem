/**
 * Created by admin on 2016/10/8.
 */
import React from 'react'
import {Carousel} from 'antd';
class Gallery extends React.Component {
  onChange(a, b, c) {
    console.log(a, b, c);
  }

  render() {
    return (
      <div>
        <Carousel afterChange={this.onChange}>
          <div><h3>About FUNTEST</h3></div>
          <div><h3>About 数据分析</h3></div>
          <div><h3>广告1</h3></div>

        </Carousel>
      </div>
    )
  }
}

export default Gallery;
