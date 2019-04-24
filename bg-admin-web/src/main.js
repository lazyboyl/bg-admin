// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// 引入国际化包
import i18n from './local'
import iView from 'iview'
import 'iview/dist/styles/iview.css'

Vue.config.productionTip = false
// 引入view的国际化
Vue.use(iView, {
  i18n: (key, value) => i18n.t(key, value)
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  // 国际化初始化
  i18n,
  components: { App },
  template: '<App/>'
})
