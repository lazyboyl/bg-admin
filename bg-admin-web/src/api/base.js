import axios from '../lib/api.request';
import qs from 'qs';
import config from '../config/run.config';
let Axios
if(config.runConfig.mock){
  Axios = require('axios');
  Axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
}
export function fetch(url, params = {}) {
  if(config.runConfig.mock){
    return new Promise((resolve, reject) => {
      Axios.post(url, params)
        .then(response => {
          resolve(response.data);
        })
        .catch((error) => {
          reject(error);
        })
    })
  }else{
    return axios.request({
      url: url,
      data:qs.stringify(params),
      method: 'post'
    });
  }
}
