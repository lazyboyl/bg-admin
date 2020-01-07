import axios from '../lib/api.request';
import qs from 'qs';
import config from '../config/run.config';
import userStore from '../store/module/user';

let Axios
if (config.runConfig.mock) {
  Axios = require('axios');
  Axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
}

// 是否需要刷新token的标志，防止在刷新token的时候，发送的请求到后端导致最后退出系统
let isRefresh = false;

export function fetch(url, params = {}) {
  if (config.runConfig.mock) {
    return new Promise((resolve, reject) => {
      Axios.post(url, params)
        .then(response => {
          resolve(response.data);
        })
        .catch((error) => {
          reject(error);
        })
    })
  } else {
    return new Promise((resolve, reject) => {
      // 如果是在刷新token，那么这时候其他请求将会被1000毫秒以后再次触发
      if(isRefresh){
        setTimeout(()=>{
          axiosPost(url,params,resolve);
        },1000)
      }else{
        // 请求之前判断当前token是否已经过期，若过期则后续的请求将被挂起。
        if(userStore.state.expireDate !='' && new Date().getTime() - userStore.state.expireDate > 0){
          isRefresh = true;
        }
        axiosPost(url,params,resolve)
      }
    });
  }
}

// 递归调用，保证在token过期刷新token的时候可以实现请求的二次发送
function axiosPost(url,params,resolve){
  axios.request({
    url: url,
    data: qs.stringify(params),
    method: 'post'
  }).then(res => {
    if (res.code == 409) {
      isRefresh = true;
      setTimeout(() => {
        isRefresh = false;
        axiosPost(url,params,resolve)
      }, 1000)
    }else{
      resolve(res);
    }
  })
}

