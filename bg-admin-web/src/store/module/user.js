import { login,getUserInfo } from '../../api/sys/user/user.api';
import router from '../../router/router';
import {getLoginMenuList} from '../../lib/util'

export default {
  state: {
    loginAccount: localStorage.getItem('loginAccount') ? localStorage.getItem('loginAccount') : '',
    nickName: localStorage.getItem('nickName') ? localStorage.getItem('nickName') : '',
    token: localStorage.getItem('token') ? localStorage.getItem('token') : '',
    headImg: localStorage.getItem('headImg') ? localStorage.getItem('headImg') : '',
    userId: localStorage.getItem('userId') ? localStorage.getItem('userId') : '',
    menuList: [],// 右侧的显示的菜单栏的数据
    access:localStorage.getItem('access') ? localStorage.getItem('access') : ''// 登录成功以后，当前用户拥有的权限的数据
  },
  getters: {
    token(state, getters, rootState) {
      return rootState.user.token;
    },
    menuList(state, getters, rootState){
      return getLoginMenuList(router, rootState.user.access);
    },
    nickName(state, getters, rootState){
      return rootState.user.nickName;
    }
  },
  mutations: {
    setLoginAccount(state, loginAccount) {
      state.loginAccount = loginAccount;
    },
    setHeadImg(state, headImg) {
      state.headImg = headImg;
    },
    setNickName(state, nickName) {
      state.nickName = nickName;
    },
    setToken(state, token) {
      state.token = token;
    },
    setRefreshToken(state, refreshToken) {
      state.refreshToken = refreshToken;
    },
    setUserId(state, userId){
      state.userId = userId;
    },
    setAccess(state,access){
      state.access = access;
    }
  },
  actions: {
    handleLogOut ({  commit }) {
      return new Promise((resolve, reject) => {
        localStorage.setItem('token',  '');
        commit('setToken', '');
        localStorage.setItem('headImg',  '');
        commit('setHeadImg', '');
        localStorage.setItem('nickName',  '');
        commit('setNickName',  '');
        localStorage.setItem('userId', '');
        commit('setUserId',  '');
        localStorage.setItem('loginAccount',  '');
        commit('setLoginAccount',  '');
        localStorage.setItem('access', []);
        commit('setAccess',  []);
      })
    },
    handleLogin ({ commit }, {loginAccount, loginPassword}) {
      loginAccount = loginAccount.trim();
      return new Promise((resolve, reject) => {
        login({
          loginAccount,
          loginPassword
        }).then(res => {
          if(res.code!=200){
            commit('setMsg', res.msg);
          }else{
            localStorage.setItem('token',  res.obj.token);
            localStorage.setItem('refreshToken',  res.obj.refreshToken);
            commit('setToken', res.obj.token);
            commit('setRefreshToken', res.obj.refreshToken);
          }
          resolve(res);
        }).catch(err => {
          reject(err);
        });
      })
    },
    getUserInfo ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getUserInfo({token:state.token}).then(res => {
          if(res.code==200){
            localStorage.setItem('headImg',   res.obj.headImg);
            commit('setHeadImg', res.obj.headImg);
            localStorage.setItem('nickName',   res.obj.nickName);
            commit('setNickName',  res.obj.nickName);
            localStorage.setItem('userId', res.obj.userId);
            commit('setUserId',  res.obj.userId);
            localStorage.setItem('loginAccount',  res.obj.loginAccount);
            commit('setLoginAccount',  res.obj.loginAccount);
            localStorage.setItem('access',  res.obj.access);
            commit('setAccess',  res.obj.access);
          }
          resolve(res);
        }).catch(err => {
          reject(err);
        });
      });
    }
  }
}
