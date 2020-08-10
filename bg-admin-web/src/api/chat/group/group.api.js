import {fetch} from '../../base';

// 查询群消息
export const loadMoreMessage = params => {
  return fetch('/group/loadMoreMessage', params);
};

// 将用户从群组中移除
export const removeUserGroup = params => {
  return fetch('/group/removeUserGroup', params);
};

// 用户审核邀请入群
export const userVerify = params => {
  return fetch('/group/userVerify', params);
};

// 群主审核入群申请
export const groupVerify = params => {
  return fetch('/group/groupVerify', params);
};

// 申请入群的note
export const applyGroup = params => {
  return fetch('/group/applyGroup', params);
};

// 邀请入群
export const invitationGroup = params => {
  return fetch('/group/invitationGroup', params);
};

// 查询群消息
export const loadMoreMessage = params => {
  return fetch('/group/loadMoreMessage', params);
};

// 删除群组
export const deleteGroup = params => {
  return fetch('/group/deleteGroup', params);
};

// 更新群组
export const updateGroup = params => {
  return fetch('/group/updateGroup', params);
};

// 获取群组
export const getGroup = params => {
  return fetch('/group/getGroup', params);
};


// 增加群组
export const addGroup = params => {
  return fetch('/group/addGroup', params);
};



