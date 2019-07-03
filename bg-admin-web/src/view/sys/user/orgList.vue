<template>
  <div>
    <Card title="组织用户">
      <Row>
        <Col span="6">
          <Card style="min-height:300px;height: calc(100vh - 274px);">
            <ButtonGroup>
              <Button type="success" @click="handleAdd" size="small">增加</Button>
              <Button type="primary" @click="handleUpdate" size="small">修改</Button>
              <Button type="error" @click="handleDelete" size="small">删除</Button>
            </ButtonGroup>
            <Tree :data="orgDate" @on-select-change="onSelectChange"></Tree>
          </Card>
        </Col>
        <Col span="17" offset="1">
          <Card :title="parentOrgName">
            <div>
              <div style="display:inline-block;float:left;">
                <Button type="success" @click="handleUserAdd">+新增用户</Button>
              </div>
              <div style="display:inline-block;float:right;">
                <Input v-model="search" suffix="ios-search" placeholder="请输入相应的查询信息" style="width: auto"
                       :search=true @on-search="handleSearch"/>
              </div>
            </div>
            <div style="clear: both;"></div>
            <div style="margin-top: 10px;">
              <Table ref="userTable" @on-sort-change="onSortChange" :columns="columns" :data="userData" :height="tableHeight">
                <template slot-scope="{ row, index }" slot="action">
                  <div >
                    <Button type="success" @click="handleEditUser(row, index)" size="small">编辑</Button>
                    <Button type="error" @click="handleDeleteUser(row)" size="small">删除</Button>
                  </div>
                </template>
              </Table>
            </div>
            <div style="margin-top: 10px;">
              <Page show-elevator show-sizer show-total :total="total" :current="current"
                    :page-size="pageSize" @on-change="changePage" @on-page-size-change="changePageSize"/>
            </div>
          </Card>
        </Col>
      </Row>
    </Card>
    <addOrg v-model="addOrgShow" :parentOrgId="parentOrgId" :parentOrgName="parentOrgName"
            v-on:reloadTree="initTree"></addOrg>
    <updateOrg v-model="updateOrgShow" :orgId="parentOrgId" v-on:reloadTree="initTree">
    </updateOrg>
    <addUser v-model="addUserShow"  v-on:handleSearch="handleSearch"></addUser>
    <updateUser v-model="updateUserShow" :userId="userId" v-on:handleSearch="handleSearch"></updateUser>
  </div>
</template>
<script>

  import {
    getOrgTree,
    deleteOrg
  } from "../../../api/sys/org/org.api"

  import {
    queryUserList,
    deleteUser
  } from "../../../api/sys/user/user.api"

  import addOrg from './addOrg'
  import updateOrg from './updateOrg'
  import addUser from './addUser'
  import updateUser from './updateUser'

  export default {
    components: {
      addOrg,
      updateOrg,
      addUser,
      updateUser
    },
    data() {
      return {
        search: '',
        columns: [
          {
            title: '用户账号',
            key: 'loginAccount',
            sortable: true
          },
          {
            title: '真实名称',
            key: 'nickName',
            sortable: true
          },
          {
            title: '创建时间',
            key: 'crtDate',
            sortable: true,
            render: (h, params) => {
              return h('div',
                this.formatDate(new Date(params.row.crtDate), 'yyyy/MM/dd hh:mm:ss')
              )
            }
          },
          {
            title: '最后登录时间',
            key: 'lastLoginDate',
            sortable: true,
            render: (h, params) => {
              if(params.row.lastLoginDate!=null){
                return h('div',
                  this.formatDate(new Date(params.row.lastLoginDate), 'yyyy/MM/dd hh:mm:ss')
                )
              }else{
                return h('div', '')
              }

            }
          },
          {
            title: '操作',
            slot: 'action'
          }
        ],
        userData: [],
        key: '',
        order: '',
        total: 0,
        current: 1,
        pageSize: 10,
        orgDate: [],
        hasChildren: false,
        parentOrgId: 0,
        parentOrgName: '虚拟顶级组织',
        fullPath: '',
        editIndex: -1,
        addOrgShow: false,
        updateOrgShow: false,
        addUserShow: false,
        updateUserShow: false,
        userId:'',
        tableHeight:200
      }
    },
    methods: {
      handleDeleteUser(row) {
        this.$Modal.confirm({
          title: '删除用户',
          content: '<p>是否删除当前用户</p>',
          onOk: () => {
            deleteUser({userId: row.userId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.handleSearch();
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info('取消删除请求');
          }
        });
      },
      handleEditUser(row, index) {
        this.userId = row.userId;
        this.updateUserShow = true;
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        let fullPath = this.fullPath
        const _this = this;
        queryUserList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue,
          fullPath
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(res.msg)
            _this.total = res.obj.total
            _this.userData = res.obj.rows
          } else {
            this.$Message.error( res.msg)
          }
        })
      },
      handleUserAdd() {
        this.addUserShow = true
      },
      handleAdd() {
        this.addOrgShow = true
      },
      handleUpdate() {
        if (this.parentOrgId == 0) {
          this.$Message.warning('请选择需要修改的组织架构节点');
          return;
        }
        this.updateOrgShow = true
      },
      handleDelete() {
        if (this.parentOrgId == 0) {
          this.$Message.warning('请选择需要删除的组织架构节点');
          return;
        }
        this.$Modal.confirm({
          title: '删除组织',
          content: '<p>是否删除当前选中的组织</p>',
          onOk: () => {
            deleteOrg({orgId: this.parentOrgId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.parentOrgId = 0;
                this.parentOrgName = '虚拟顶级组织';
                // 删除数据成功同时刷新grid
                this.initTree();
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info('取消了删除请求');
          }
        });
      },
      initTree() {
        const _this = this;
        getOrgTree({}).then(res => {
          if (res.code == 200) {
            _this.orgDate = res.obj;
          } else {
            this.$Message.error("加载组织架构菜单节点失败，请与管理员联系！")
          }
        });
      },
      onSelectChange(data) {
        // 如果长度为0说明当前没有任何节点被选中
        if (data.length == 0) {
          this.parentOrgId = 0;
          this.parentOrgName = '虚拟顶级组织';
          this.hasChildren = false;
          this.fullPath = '';
        } else {
          this.parentOrgId = data[0].orgId;
          this.parentOrgName = data[0].title;
          this.fullPath = data[0].fullPath;
          if (data[0].children.length == 0) {
            this.hasChildren = false;
          } else {
            this.hasChildren = true;
          }
        }
      },
      onSortChange(sort) {
        if (sort.order == 'normal') {
          this.order = '';
        } else {
          this.key = sort.key;
          this.order = sort.order;
        }
        this.handleSearch();
      },
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
        this.handleSearch();
      }
    },
    mounted() {
      // 初始化完成以后加载菜单数据
      this.initTree();
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.userTable.$el.offsetTop - 335
    },
    watch: {
      parentOrgId() {
        this.handleSearch();
      }
    }
  }

</script>
