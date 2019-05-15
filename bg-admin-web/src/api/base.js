import axios from '../lib/api.request';
import qs from 'qs';

// 通用的远程访问的方法
export function fetch(url, params = {}) {
    return axios.request({
        // 响应的后端的接口的地址，不包含http://127.0.0.1:8288这一部分，因此这一部分我们已经在axios.js中配置了
        url: url,
        // 由于我们的数据的提交方式是form的方式提交，因此需要对入参建转换
        data:qs.stringify(params),
        // 数据请求的方式为post
        method: 'post'
    });
}
