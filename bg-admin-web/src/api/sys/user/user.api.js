import {fetch} from '../../base';

// 根据token和旧的密码来更新新的密码
export const changePassword = params => {
  return fetch('/user/changePassword',params);
};

// 删除用户信息
export const deleteUser = params => {
  return fetch('/user/deleteUser',params);
};

// 更新用户信息
export const updateUser = params => {
  return fetch('/user/updateUser',params);
};

// 根据用户流水ID来获取用户数据
export const getUserByUserId = params => {
  return fetch('/user/getUserByUserId',params);
};

// 验证这个账户是否已经创建过
export const checkLoginAccount = params => {
  return fetch('/user/checkLoginAccount',params);
};

// 创建用户
export const createUser = params => {
  return fetch('/user/createUser',params);
};

// 加载所有的角色数据
export const loadAllRole = params => {
  return fetch('/user/loadAllRole',params);
};

// 获取用户列表
export const queryUserList = params => {
  return fetch('/user/queryUserList',params);
};

// 实现用户登录
export const login = params => {
  return fetch('/user/login',params);
};

// 根据token来获取用户信息
export const getUserInfo = params => {
  return fetch('/user/getUserInfo',params);
};
