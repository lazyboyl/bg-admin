<template>
  <div>
    <Card title="角色管理">
      <div>
        <div style="display:inline-block;float:left;">
          <Button type="success" @click="addRole">+新增角色</Button>
        </div>
        <div style="display:inline-block;float:right;">
          <Input v-model="search" suffix="ios-search" placeholder="请输入查询信息" style="width: auto"
                 :search=true @on-search="handleSearch"/>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table ref="roleTable" @on-sort-change="onSortChange" :columns="columns" :data="roleData" :height="tableHeight">
          <template slot-scope="{ row, index }" slot="roleName">
            <Input type="text" v-model="editRoleName" v-if="editIndex === index"/>
            <span v-else>{{ row.roleName }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="roleCode">
            <Input type="text" v-model="editRoleCode" v-if="editIndex === index"/>
            <span v-else>{{ row.roleCode }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="action">
            <div v-if="editIndex === index">
              <Button size="small" type="success" @click="handleUpdate(index)">保存</Button>
              <Button size="small" type="error" @click="editIndex = -1">取消</Button>
            </div>
            <div v-else>
              <Button type="success" @click="handleEdit(row, index)" size="small">编辑</Button>
              <Button type="primary" @click="handleModify(row)" size="small">修改</Button>
              <Button type="error" @click="handleDelete(row, index)" size="small">删除</Button>
            </div>
          </template>
        </Table>
      </div>
      <div style="margin-top: 10px;">
        <Page show-elevator show-sizer show-total :total="total" :current="current"
              :page-size="pageSize" @on-change="changePage" @on-page-size-change="changePageSize"/>
      </div>
    </Card>
  </div>
</template>
<script>
  import {queryRoleList, updateRole, deleteRole} from '../../../api/sys/role/role.api'

  export default {
    data() {
      return {
        addShow: false,
        editShow :false,
        roleId:'',
        search: '',
        roleData: [],
        columns: [
          {
            title: '角色名称',
            slot: 'roleName',
            key: 'roleName',
            sortable: true
          },
          {
            title: '角色编码',
            slot: 'roleCode',
            key: 'roleCode',
            sortable: true
          },
          {
            title: '创建时间',
            key: 'crtDate',
            sortable: true,
            render: (h,params)=>{
              return h('div',
                this.formatDate(new Date(params.row.crtDate),'yyyy/MM/dd hh:mm:ss')
              )
            }
          },
          {
            title: '操作',
            slot: 'action'
          }
        ],
        total: 0,
        current: 1,
        pageSize: 10,
        key:'crtDate',
        order:'desc',
        editIndex: -1,  // 当前聚焦的输入框的行数
        editRoleId: '',
        editRoleName: '',
        editRoleCode: '',
        tableHeight:200
      }
    },
    methods: {
      addRole() {
        this.addShow = true
      },
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
        this.handleSearch();
      },
      onSortChange(sort){
        if(sort.order=='normal'){
          this.order = '';
        }else{
          this.key = sort.key;
          this.order = sort.order;
        }
        this.handleSearch();
      },
      handleDelete(row, index) {
        this.$Modal.confirm({
          title: '删除角色',
          content: '<p>是否删除当前选中的角色</p>',
          onOk: () => {
            deleteRole({roleId: row.roleId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 删除数据成功同时刷新grid
                this.handleSearch();
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info('取消');
          }
        });
      },
      handleUpdate(index) {
        updateRole({roleId: this.editRoleId, roleName: this.editRoleName, roleCode: this.editRoleCode}).then(res => {
          if (res.code == 200) {
            this.$Message.success(res.msg)
            this.editIndex = -1
            this.handleSearch()
          } else {
            this.$Message.error(res.msg)
          }
        });
      },
      handleModify(row){
        this.roleId = row.roleId
        this.editShow = true
      },
      handleEdit(row, index) {
        this.editRoleName = row.roleName;
        this.editRoleCode = row.roleCode;
        this.editRoleId = row.roleId
        this.editIndex = index
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        const _this = this;
        queryRoleList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(res.msg)
            _this.total = res.obj.total
            _this.roleData = res.obj.rows
          } else {
            this.$Message.error(res.msg)
          }
        });
      }
    },
    mounted() {
      // 初始化完成组件的时候执行以下的逻辑
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.roleTable.$el.offsetTop - 270
    }
  }
</script>
