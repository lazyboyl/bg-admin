import Vue from 'vue';
import iView from 'iview';
import Util from '../lib/util';
import VueRouter from 'vue-router';
import routers from './router';

Vue.use(VueRouter);

// 路由配置
const RouterConfig = {
  //mode: 'history',
  routes: routers
};

export const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start();
  Util.title(to.meta.title);
  if (to.meta.requireAuth) {
    if (localStorage.getItem('token') != '' && localStorage.getItem('token') != null) {
      next();
    } else {
      next({
        path: '/login',
        query: {redirect: to.fullPath}//登录成功以后跳转到该路由
      });
    }
  } else {
    next();
  }
});

router.afterEach((to) => {
  iView.LoadingBar.finish();
  window.scrollTo(0, 0);
});

