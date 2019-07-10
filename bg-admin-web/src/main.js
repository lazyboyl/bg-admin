// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import {router} from './router/index'
// 引入国际化包
import i18n from './local'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import runConfig from './config/run.config';
import iviewArea from 'iview-area';
import store from './store';
import JsEncrypt from 'jsencrypt';

/**
 * 生成唯一的uuid
 * @returns {string}
 */
Vue.prototype.$uuid = function(){
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    let r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
    return v.toString(16);
  });
};

/**
 * 配置全局的加密方法
 * @param obj 需要加密的字符串
 */
Vue.prototype.$encruption = function (obj) {
  let encrypt = new JsEncrypt();
  encrypt.setPublicKey(
    `-----BEGIN PUBLIC KEY-----MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDyKmfJhgAHEK0Yo6qOtFB8sSfYIZBJUyyXi1X7EgkZo3kmMOu6/uT7SwC9EnqbiMySJfAlvB200ZMIk6e1NQrPLZpc5VSuzSQ3NqdXOdbNnhXAUeME8IcsbscK9XP8BxldMhUhIK+3zovI1VCY2kLO6TMwEVvTdH+F1jW/WFkplwIDAQAB-----END PUBLIC KEY-----`
  )
  return encrypt.encrypt(obj);
};

Vue.use(iviewArea);

/**
 * @description 全局注册应用配置
 */
Vue.prototype.$runConfig = runConfig;

/**
 * 表示当前的应用启动的时候是以mock的方式启动
 */
if (runConfig.runConfig.mock) {
  require('./config/mock/mock.js')
}

Vue.config.productionTip = false
// 引入view的国际化
Vue.use(iView, {
  i18n: (key, value) => i18n.t(key, value)
})

// 格式化时间全局通用方法
Vue.prototype.formatDate = function (date, fmt) {
  let o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'S': date.getMilliseconds() // 毫秒
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  // 国际化初始化
  i18n,
  store: store,
  components: {App},
  template: '<App/>'
})
