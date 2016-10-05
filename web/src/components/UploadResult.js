import React from 'react';

class UploadResult extends React.Component {
	render(){
		return (
			<div>
				处理完毕，查看报告{this.props.result}
			</div>
			)
	}
}

export default UploadResult;