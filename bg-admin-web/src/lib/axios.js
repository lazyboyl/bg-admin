import Axios from 'axios';
import {Message} from 'iview';

class httpRequest {

  constructor() {
    this.options = {
      method: '',
      url: ''
    };
    // 存储请求队列
    this.queue = {};
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
      // 在往后端发送请求的时候，还没有发送之前的处理，
      // 比如我们需要往我们的请求的header里面添加一些token或者其他信息的时候可以在此处进行相应的操作处理
      console.log('请求拦截')
      return config
    }, error => {
      // 对请求错误做些什么
      return Promise.reject(error);
    })

    // 添加响应拦截器
    instance.interceptors.response.use((res) => {
      // 已经完成了和后端的交互，此处已经获取到了后端返回的请求了，
      // 可以在此处做权限的验证拦截，比如用户非法登录的时候可以在此处重定向到登录页面
      let {data} = res;
      console.log('响应拦截'+res)
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
      // 远程请求的服务的地址
      baseURL: "http://127.0.0.1:8288",
      // 超时时间
      timeout: 5000,
      headers: {
        // 设置前端的跨域
        'Access-Control-Allow-Origin': '*',
        // 数据的提交方式为form的方式提交，若需要使用json的模式提交则将Content-Type值改为如下
        // application/json; charset=utf-8
        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
        'X-URL-PATH': location.pathname
      }
    };
    return Axios.create(conf);
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
