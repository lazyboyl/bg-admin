import {fetch} from '../../base';

// 验证字典的类型和编码是否重复
export const checkTypeAndCode = params => {
  return fetch('/dict/checkTypeAndCode',params);
};

// 增加数据字典的数据
export const addDict = params => {
  return fetch('/dict/addDict',params);
};

// 删除数据字典的数据
export const deleteDict = params => {
  return fetch('/dict/deleteDict',params);
};

// 更新数据字典的数据
export const updateDict = params => {
  return fetch('/dict/updateDict',params);
};

// 获取数据字典列表数据
export const queryDictList = params => {
  return fetch('/dict/queryDictList',params);
};
