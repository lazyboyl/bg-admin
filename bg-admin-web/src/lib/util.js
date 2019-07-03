let util = {

};
util.title = function(title) {
  title = title ? title  : 'iView project';
  window.document.title = title;
};

export const forEach = (arr, fn) => {
  if (!arr.length || !fn) return;
  let i = -1;
  let len = arr.length;
  while (++i < len) {
    let item = arr[i];
    fn(item, i, arr);
  }
};

/**
 * 获取当前登陆的用户的权限集合数据
 * @param list {Array} 所有的路由集合数据
 * @param access {Array} 当前允许登陆的路由集合数据
 */
export const getLoginMenuList = (list, access) =>{
  let res = [];
  forEach(list, item => {
    if (!item.meta || (item.meta && !item.meta.hideInMenu)) {
      let obj = {
        icon: (item.meta && item.meta.icon) || '',
        name: item.name,
        meta: item.meta,
        code: item.code,
        path: item.path
      };
      if(hasChild(item)&&showThisMenuEle(item,access)){
        obj.children = getLoginMenuList(item.children, access);
      }
      if (showThisMenuEle(item, access)) res.push(obj);
    }
  })
  return res;
};

const showThisMenuEle = (item, access) => {
  let code = item.meta.code;
  if(access.indexOf(code)!=-1){
     return true;
  }else{
    return false;
  }
}

export const hasChild = (item) => {
  return item.children !=undefined && item.children.length !== 0
};

/**
 * @param {Array} routeMetched 当前路由metched
 * @returns {Array}
 */
export const getBreadCrumbList = (routeMetched, homeRoute) => {
  let res = routeMetched.filter(item => {
    return item.meta === undefined || !item.meta.hide
  }).map(item => {
    let obj = {
      icon: (item.meta && item.meta.icon) || '',
      name: item.name,
      meta: item.meta
    };
    return obj
  });
  res = res.filter(item => {
    return !item.meta.hideInMenu
  });
  return [Object.assign(homeRoute, { to: homeRoute.path }), ...res];
};

/**
 * @param {Array} routers 路由列表数组
 * @description 用于找到路由列表中name为home的对象
 */
export const getHomeRoute = routers => {
  let i = -1;
  let len = routers.length;
  let homeRoute = {};
  while (++i < len) {
    let item = routers[i];
    if (item.children && item.children.length) {
      let res = getHomeRoute(item.children);
      if (res.name) return res;
    } else {
      if (item.name === 'home') homeRoute = item;
    }
  }
  return homeRoute;
};

export default util;
