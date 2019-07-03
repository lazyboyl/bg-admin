import {getBreadCrumbList, getHomeRoute} from '../../lib/util';
import router from '../../router/router';

export default {
  state: {
    breadCrumbList: localStorage.getItem('breadCrumbList') ? JSON.parse(localStorage.getItem('breadCrumbList')) : [],
    homeRoute: getHomeRoute(router)
  },
  getters: {
    breadCrumbList(state, getters, rootState) {
      return rootState.app.breadCrumbList;
    },
  },
  mutations: {
    setBreadCrumb(state, routeMetched) {
      let breadCrumbList = getBreadCrumbList(routeMetched, state.homeRoute);
      state.breadCrumbList = breadCrumbList;
      localStorage.setItem('breadCrumbList', JSON.stringify(breadCrumbList));
    }
  }
}
