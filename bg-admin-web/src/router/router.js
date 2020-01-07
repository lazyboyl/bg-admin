import main from '../view/main/main.vue';

export default [
  {
    path: '/',
    name: 'login_default',
    meta: {
      icon: 'ios-settings',
      hideInMenu: true,
      title: '登陆页',
      requireAuth: false //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
    },
    component: resolve => {
      require(['../view/login/login.vue'], resolve);
    }
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      icon: 'ios-settings',
      hideInMenu: true,
      title: '登陆页',
      requireAuth: false //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
    },
    component: resolve => {
      require(['../view/login/login.vue'], resolve);
    }
  },
  {
    path: '/main',
    name: 'main',
    meta: {
      icon: 'ios-settings',
      title: '系统首页',
      hideInMenu: true,
      requireAuth: false //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
    },
    component: resolve => {
      require(['../view/main/main.vue'], resolve);
    }
  },
  {
    path: '/test',
    name: 'test',
    component: main,
    meta: {
      icon: 'ios-construct',
      title: '系统管理',
      code: 'system-manage-test',
      requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
    },
    children: [
      {
        path: 'testList',
        name: 'testList',
        meta: {
          icon: 'ios-paper',
          title: '字典维护',
          code: 'system-manage-test-list',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/tTest/tTestList.vue'], resolve);
        }
      }
    ]
  },
  {
    path: '/sys',
    name: 'sys',
    component: main,
    meta: {
      icon: 'ios-construct',
      title: '系统管理',
      code: 'system-manage',
      requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
    },
    children: [
      {
        path: 'dictList',
        name: 'dictList',
        meta: {
          icon: 'ios-paper',
          title: '字典维护',
          code: 'system-manage-dict',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/dict/dictList.vue'], resolve);
        }
      },
      {
        path: 'treeList',
        name: 'treeList',
        meta: {
          icon: 'md-git-network',
          title: '菜单管理',
          code: 'system-manage-tree',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/tree/treeList.vue'], resolve);
        }
      },
      {
        path: 'roleList',
        name: 'roleList',
        meta: {
          icon: 'ios-cog',
          title: '角色管理',
          code: 'system-manage-role',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/role/roleList.vue'], resolve);
        }
      },
      {
        path: 'orgList',
        name: 'orgList',
        meta: {
          icon: 'ios-people',
          title: '用户组织',
          code: 'system-manage-user',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/user/orgList.vue'], resolve);
        }
      },
      {
        path: 'msgList',
        name: 'msgList',
        meta: {
          icon: 'ios-chatbubbles',
          title: '消息管理',
          code: 'system-manage-message',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/msg/msgList.vue'], resolve);
        }
      },
      {
        path: 'behaviorList',
        name: 'behaviorList',
        meta: {
          icon: 'ios-browsers',
          title: '行为日志',
          code: 'system-manage-behavior',
          requireAuth: true //表示当前响应的请求是否需要进行登录拦截验证【true：需要；false：不需要】
        },
        component: resolve => {
          require(['../view/sys/behavior/behaviorList.vue'], resolve);
        }
      }
    ]
  }
]

