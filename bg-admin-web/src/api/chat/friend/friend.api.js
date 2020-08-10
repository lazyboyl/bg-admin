import {fetch} from '../../base';

// 获取我的好友的分组列表的数据
export const getFriendGroupList = params => {
  return fetch('/friend/getFriendGroupList',params);
};

// 查询用户消息
export const loadMoreMessage = params => {
  return fetch('/friend/loadMoreMessage',params);
};

// 获取好友请求列表
export const getApplyFriendList = params => {
  return fetch('/friend/getApplyFriendList',params);
};

// 删除好友分组的时候实现分组底下好友的数据的迁移
export const deleteFriendGroup = params => {
  return fetch('/friend/deleteFriendGroup',params);
};

// 创建分组
export const createFriendGroup = params => {
  return fetch('/friend/createFriendGroup',params);
};

// 删除好友
export const deleteFriend = params => {
  return fetch('/friend/deleteFriend',params);
};

// 好友审核
export const verifyFriend = params => {
  return fetch('/friend/verifyFriend',params);
};

// 好友申请
export const applyFriend = params => {
  return fetch('/friend/applyFriend',params);
};

// 查询用户消息
export const loadMoreMessage = params => {
  return fetch('/friend/loadMoreMessage',params);
};

// 好友查询
export const queryFriend = params => {
  return fetch('/friend/queryFriend',params);
};

// 我的好友列表
export const myFriendList = params => {
  return fetch('/friend/myFriendList',params);
};
