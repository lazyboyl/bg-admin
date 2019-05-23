import Vue from 'vue'
import Router from 'vue-router'
import main from '../view/main/main.vue';

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'login',
      component: resolve => {
        require(['../view/login/login.vue'], resolve);
      }
    },
    {
      path: '/main',
      name: 'main',
      meta: {
        icon: 'ios-settings',
        title: '系统首页',
        hideInMenu: true,
        requireAuth: false //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
      },
      component: resolve => {
        require(['../view/main/main.vue'], resolve);
      }
    },
    {
      path: '/sys',
      name: 'sys',
      component: main,
      meta: {
        icon: 'ios-construct',
        title: '系统管理',
        code:'system-manage',
        requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
      },
      children:[
        {
          path: 'dictList',
          name: 'dictList',
          meta: {
            icon: 'ios-paper',
            title: '字典维护',
            code:'system-manage-dict',
            requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
          },
          component: resolve => {
            require(['../view/sys/dict/dictList.vue'], resolve);
          }
        }
      ]
    }
  ]
})
