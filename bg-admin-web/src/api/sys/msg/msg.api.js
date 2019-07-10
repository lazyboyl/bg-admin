import {fetch} from '../../base';

// 实现消息的阅读
export const readMsg = params => {
  return fetch('/msg/readMsg',params);
};

// 获取当前登录的用户的未读的消息
export const queryUserMsg = params => {
  return fetch('/msg/queryUserMsg',params);
};

// 发布消息
export const publishNews = params => {
  return fetch('/msg/publishNews',params);
};

// 获取消息列表
export const queryMessageList = params => {
  return fetch('/msg/queryMessageList',params);
};
