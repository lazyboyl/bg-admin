import {fetch} from '../../base';

// 获取行为日志列表
export const queryBehaviorLogList = params => {
  return fetch('/behaviorLog/queryBehaviorLogList',params);
};
