import {fetch} from '../../base';

// 获取组织架构的Cascader的数据
export const getOrgCascader = params => {
  return fetch('/org/getOrgCascader', params);
};

// 获取组织架构信息
export const getOrgByOrgId = params => {
  return fetch('/org/getOrgByOrgId', params);
};

// 验证组织架构的名称和编码
export const checkOrgNameAndCode = params => {
  return fetch('/org/checkOrgNameAndCode', params);
};

// 删除组织架构
export const deleteOrg = params => {
  return fetch('/org/deleteOrg', params);
};

// 更新组织架构的数据
export const updateOrg = params => {
  return fetch('/org/updateOrg', params);
};

// 增加组织架构
export const addOrg = params => {
  return fetch('/org/addOrg', params);
};

// 获取组织架构树目录
export const getOrgTree = params => {
  return fetch('/org/getOrgTree', params);
};
