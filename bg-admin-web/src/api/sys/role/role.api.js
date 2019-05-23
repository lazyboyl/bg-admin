import {fetch} from '../../base';

// 加载菜单节点的数据
export const loadTree = params => {
  return fetch('/role/loadTree',params);
};

// 删除角色信息
export const deleteRole = params => {
  return fetch('/role/deleteRole',params);
};

// 更新角色信息
export const updateRole = params => {
  return fetch('/role/updateRole',params);
};

// 获取角色信息
export const getRoleByRoleId = params => {
  return fetch('/role/getRoleByRoleId',params);
};

// 增加角色
export const addRole  = params => {
  return fetch('/role/addRole',params);
};

// 验证角色编码和角色名字是否重复
export const checkRoleCodeAndName  = params => {
  return fetch('/role/checkRoleCodeAndName',params);
};

// 获取角色列表
export const queryRoleList = params => {
  return fetch('/role/queryRoleList',params);
};

