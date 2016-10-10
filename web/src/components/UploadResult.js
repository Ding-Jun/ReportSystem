import React from 'react';
import {Link} from 'react-router'
class UploadResult extends React.Component {
	render(){
		return (
			<div>
				处理完毕，<Link to={"/report/"+this.props.result}>查看报告</Link>
			</div>
			)
	}
}

export default UploadResult;
