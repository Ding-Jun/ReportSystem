import {
	message
} from 'antd'
export default class Messager {
	static success(content, duration) {
		message.success(content, duration);
	}

	static info(content, duration) {
		message.info(content, duration);
	}
	static error(content, duration) {
		message.error(content, duration);
	}
	static loading(content, duration) {
		message.loading(content, duration);
	}
	static warning(content, duration) {
		message.warning(content, duration);
	}

	static config(option) {
		message.config(option)
	}
}