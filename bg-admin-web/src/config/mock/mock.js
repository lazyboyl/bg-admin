// 引入mockjs
const Mock = require('mockjs');
import userLogin from './json/user/user.login';
import userGetUserInfo from './json/user/user.getUserInfo';
import userGetOrgTree from './json/user/user.getOrgTree';
import userQueryUserList from './json/user/user.queryUserList';
import userCheckOrgNameAndCode from './json/user/user.checkOrgNameAndCode';
import userAddOrg from './json/user/user.addOrg';
import userGetOrgByOrgId from './json/user/user.getOrgByOrgId';
import userUpdateOrg from './json/user/user.updateOrg';
import userDeleteOrg from './json/user/user.deleteOrg';
import userGetOrgCascader from './json/user/user.getOrgCascader';
import userLoadAllRole from './json/user/user.loadAllRole';
import userCheckLoginAccount from './json/user/user.checkLoginAccount';
import userCcreateUser from './json/user/user.createUser';
import userGetUserByUserId from './json/user/user.getUserByUserId';
import userUpdateUser from './json/user/user.updateUser';
import userDeleteUser from './json/user/user.deleteUser';
import changePassword from './json/user/user.changePassword'

Mock.mock('/user/login', 'post', userLogin);
Mock.mock('/user/getUserInfo', 'post', userGetUserInfo);
Mock.mock('/org/getOrgTree', 'post', userGetOrgTree);
Mock.mock('/user/queryUserList', 'post', userQueryUserList);
Mock.mock('/org/checkOrgNameAndCode', 'post', userCheckOrgNameAndCode);
Mock.mock('/org/addOrg', 'post', userAddOrg);
Mock.mock('/org/getOrgByOrgId', 'post', userGetOrgByOrgId);
Mock.mock('/org/updateOrg', 'post', userUpdateOrg);
Mock.mock('/org/deleteOrg', 'post', userDeleteOrg);
Mock.mock('/org/getOrgCascader', 'post', userGetOrgCascader);
Mock.mock('/user/loadAllRole', 'post', userLoadAllRole);
Mock.mock('/user/checkLoginAccount', 'post', userCheckLoginAccount);
Mock.mock('/user/createUser', 'post', userCcreateUser);
Mock.mock('/user/getUserByUserId', 'post', userGetUserByUserId);
Mock.mock('/user/updateUser', 'post', userUpdateUser);
Mock.mock('/user/deleteUser', 'post', userDeleteUser);
Mock.mock('/user/changePassword', 'post', changePassword);

// 字典的mock
import dictQueryDictList from './json/dict/dict.queryDictList';
import dictDeleteDict from './json/dict/dict.deleteDict';
import dictUpdateDict from './json/dict/dict.updateDict';
import dictCheckTypeAndCode from './json/dict/dict.checkTypeAndCode';
import dictAddDict from './json/dict/dict.addDict';

Mock.mock('/dict/queryDictList', 'post', dictQueryDictList);
Mock.mock('/dict/deleteDict', 'post', dictDeleteDict);
Mock.mock('/dict/updateDict', 'post', dictUpdateDict);
Mock.mock('/dict/checkTypeAndCode', 'post', dictCheckTypeAndCode);
Mock.mock('/dict/addDict', 'post', dictAddDict);

// 角色的mock
import roleQueryRoleList from './json/role/role.queryRoleList';
import roleLoadTree from './json/role/role.loadTree';
import roleCheckRoleCodeAndName from './json/role/role.checkRoleCodeAndName';
import roleAddRole from './json/role/role.addRole';
import roleGetRoleByRoleId from './json/role/role.getRoleByRoleId';
import roleUpdateRole from './json/role/role.updateRole';
import roleDeleteRole from './json/role/role.deleteRole';

Mock.mock('/role/queryRoleList', 'post', roleQueryRoleList);
Mock.mock('/role/loadTree', 'post', roleLoadTree);
Mock.mock('/role/checkRoleCodeAndName', 'post', roleCheckRoleCodeAndName);
Mock.mock('/role/addRole', 'post', roleAddRole);
Mock.mock('/role/getRoleByRoleId', 'post', roleGetRoleByRoleId);
Mock.mock('/role/updateRole', 'post', roleUpdateRole);
Mock.mock('/role/deleteRole', 'post', roleDeleteRole);

// 菜单的mock
import treeGetTreeList from './json/tree/tree.getTreeList';
import treeCheckTreeCode from './json/tree/tree.checkTreeCode';
import treeAddTree from './json/tree/tree.addTree';
import treeGetTreeByTreeId from './json/tree/tree.getTreeByTreeId';
import treeUpdateTree from './json/tree/tree.updateTree';
import treeDeleteTree from './json/tree/tree.deleteTree';
import treeAddButton from './json/tree/tree.addButton';
import treeUpdateButton from './json/tree/tree.updateButton';
import treeOperateButton from './json/tree/tree.operateButton';
import treeDeleteButton from './json/tree/tree.deleteButton';
import treeQueryTreeButtonList from './json/tree/tree.queryTreeButtonList';

Mock.mock('/tree/getTreeList', 'post', treeGetTreeList);
Mock.mock('/tree/checkTreeCode', 'post', treeCheckTreeCode);
Mock.mock('/tree/addTree', 'post', treeAddTree);
Mock.mock('/tree/getTreeByTreeId', 'post', treeGetTreeByTreeId);
Mock.mock('/tree/updateTree', 'post', treeUpdateTree);
Mock.mock('/tree/deleteTree', 'post', treeDeleteTree);
Mock.mock('/tree/addButton', 'post', treeAddButton);
Mock.mock('/tree/updateButton', 'post', treeUpdateButton);
Mock.mock('/tree/operateButton', 'post', treeOperateButton);
Mock.mock('/tree/deleteButton', 'post', treeDeleteButton);
Mock.mock('/tree/queryTreeButtonList', 'post', treeQueryTreeButtonList);

// 日志行为的mock
import treeQueryBehaviorLogList from './json/behaviorLog/behaviorLog.queryBehaviorLogList';

Mock.mock('/behaviorLog/queryBehaviorLogList', 'post', treeQueryBehaviorLogList);




