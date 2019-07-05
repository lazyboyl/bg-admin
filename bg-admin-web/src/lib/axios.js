import Axios from 'axios';
import {Message} from 'iview';
import config from '../config/run.config';
import userStore from '../store/module/user';
import qs from 'qs';

class httpRequest {
  constructor() {
    this.options = {
      method: '',
      url: ''
    };
    // 存储请求队列
    this.queue = [];
  }


  // 销毁请求实例
  destroy(url) {
    delete this.queue[url];
    const queue = Object.keys(this.queue);
    return queue.length;
  }

  // 请求拦截
  interceptors(instance, url) {
    // 添加请求拦截器
    instance.interceptors.request.use(config => {
      if (userStore.state.token != '') {
        config.headers['x-access-token'] = userStore.state.token;
      }
      return config
    }, error => {
      // 对请求错误做些什么
      return Promise.reject(error);
    })

    // 添加响应拦截器
    instance.interceptors.response.use((res) => {
      let {data} = res;
      // 表示当前的操作的用户还没有登录，因此重新跳转到登录页面
      if (data.code == 401) {
        userStore.state.token = '';
        window.location.href = window.location.pathname + '#/login';
        Message.error('未登录，或登录失效，请登录');
      } else if (data.code == 409) {
        // token过期重新刷新token的值
        this.refreshToken();
      }
      return data;
    }, (error) => {
      Message.error('服务内部错误');
      // 对响应错误做点什么
      return Promise.reject(error);
    })
  }

  // 创建实例
  create() {
    let conf = {
      baseURL: config.runConfig.baseUrl,
      timeout: 5000,
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
        'X-URL-PATH': location.pathname
      }
    };
    return Axios.create(conf);
  }

  refreshToken() {
    Axios.post(config.runConfig.refreshTokenUrl, qs.stringify({refreshToken: userStore.state.refreshToken}))
      .then(response => {
        let {data} = response;
        // 表示可以重新发起请求
        if (data.code == 200) {
          userStore.state.token = data.obj.token;
          userStore.state.refreshToken = data.obj.refreshToken;
          localStorage.setItem('token', data.obj.token);
          localStorage.setItem('refreshToken', data.obj.refreshToken);
        } else {
          userStore.state.token = '';
          userStore.state.refreshToken = '';
          localStorage.setItem('token', '');
          localStorage.setItem('refreshToken', '');
          window.location.href = window.location.pathname + '#/login';
          Message.error('登录失效，请登录');
        }
      })
      .catch((error) => {
        console.log('error=>' + error)
      })
  }

  // 请求实例
  request(options) {
    let instance = this.create();
    this.interceptors(instance, options.url);
    options = Object.assign({}, options);
    this.queue[options.url] = instance;
    return instance(options);
  }
}

export default httpRequest;
