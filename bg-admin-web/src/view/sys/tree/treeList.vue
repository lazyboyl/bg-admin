<template>
  <div>
    <Card title="菜单管理">
      <Row>
        <Col span="6">
          <Card style="min-height:300px;height: calc(100vh - 274px);">
            <ButtonGroup>
              <Button type="success" @click="handleAdd" size="small">增加</Button>
              <Button type="primary" @click="handleUpdate" size="small">修改</Button>
              <Button type="error" @click="handleDelete" size="small">删除</Button>
            </ButtonGroup>
            <Tree :data="roleTreeDate" @on-select-change="onSelectChange"></Tree>
          </Card>
        </Col>
        <Col span="17" offset="1">
          <Card :title="parentTreeName">
            <div>
              <div style="display:inline-block;float:left;">
                <Button type="success" @click="handleTreeButton">+创建按钮</Button>
              </div>
              <div style="display:inline-block;float:right;">
                <Input v-model="search" suffix="ios-search" placeholder="请输入查询的条件" style="width: auto"
                       :search=true @on-search="handleSearch"/>
              </div>
            </div>
            <div style="clear: both;"></div>
            <div style="margin-top: 10px;">
              <Table ref="treeButtonTable" @on-sort-change="onSortChange" :columns="columns" :data="treeButtonData"
                     :height="tableHeight">
                <template slot-scope="{ row, index }" slot="treeName">
                  <Input type="text" v-model="editTreeName" v-if="editIndex === index"/>
                  <span v-else>{{ row.treeName }}</span>
                </template>

                <template slot-scope="{ row, index }" slot="treeCode">
                  <Input type="text" v-model="editTreeCode" v-if="editIndex === index"/>
                  <span v-else>{{ row.treeCode }}</span>
                </template>

                <template slot-scope="{ row, index }" slot="treeState">
                  <span v-if="row.treeState=='1'">可用</span>
                  <span v-else>禁用</span>
                </template>

                <template slot-scope="{ row, index }" slot="action">
                  <div v-if="editIndex === index">
                    <Button size="small" type="success" @click="handleUpdateButton(index)">更新
                    </Button>
                    <Button size="small" type="error" @click="editIndex = -1">取消</Button>
                  </div>
                  <div v-else>
                    <Button type="success" @click="handleEditButton(row, index)" size="small">编辑
                    </Button>
                    <Button type="error" @click="handleDeleteButton(row)" size="small">删除</Button>
                    <Button type="primary" v-if="row.treeState=='1'" @click="handleOperate(row,'2')" size="small">
                      冻结
                    </Button>
                    <Button type="info" v-else @click="handleOperate(row,'1')" size="small">解冻
                    </Button>
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
    <addButton v-model="addButtonShow" :parentTreeId="parentTreeId" :parentTreeName="parentTreeName"
               v-on:handleSearch="handleSearch"></addButton>
    <addTree v-model="addTreeShow" :parentTreeId="parentTreeId" :parentTreeName="parentTreeName"
             v-on:reloadTree="initTree"></addTree>
    <updateTree v-model="updateTreeShow" :treeId="parentTreeId" v-on:reloadTree="initTree"></updateTree>
    <updateButton v-model="updateButtonShow" :treeId="editTreeId" v-on:handleSearch="handleSearch"></updateButton>
  </div>
</template>
<script>
  import {
    getTreeList,
    queryTreeButtonList,
    deleteButton,
    operateButton,
    deleteTree
  } from "../../../api/sys/tree/tree.api"
  import addButton from './addButton'
  import updateButton from './updateButton'
  import addTree from './addTree'
  import updateTree from './updateTree'

  export default {
    name: 'treeList',
    components: {
      addButton,
      updateButton,
      addTree,
      updateTree
    },
    data() {
      return {
        addButtonShow: false,
        updateButtonShow: false,
        addTreeShow: false,
        updateTreeShow: false,
        roleTreeDate: [],
        hasChildren: false,
        parentTreeName: '顶层节点',
        parentTreeId: 0,
        search: '',
        key: '',
        order: '',
        total: 0,
        current: 1,
        pageSize: 10,
        columns: [
          {
            title: '按钮名称',
            slot: 'treeName',
            key: 'treeName',
            sortable: true
          }, {

            title: '按钮编码',
            slot: 'treeCode',
            key: 'treeCode',
            sortable: true
          },
          {
            title: '按钮状态',
            key: 'treeState',
            slot: 'treeState',
            sortable: true
          },
          {
            title: '操作',
            slot: 'action'
          }
        ],
        treeButtonData: [],
        editTreeName: '',
        editTreeCode: '',
        editTreeId: 0,
        editIndex: -1,
        tableHeight: 150
      }
    },
    methods: {
      handleTreeButton() {
        this.addButtonShow = true
      },
      handleOperate(row, treeState) {
        let content = '是否冻结当前按钮'
        if (treeState == '1') {
          content = '是否解冻当前按钮'
        }

        this.$Modal.confirm({
          title: this.$t('modal.title'),
          content: '<p>' + content + '</p>',
          onOk: () => {
            operateButton({treeId: row.treeId, treeState: treeState}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 冻结用户成功同时刷新grid
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
      handleDeleteButton(row) {
        this.$Modal.confirm({
          title: '删除按钮',
          content: '<p>是否删除当前按钮节点</p>',
          onOk: () => {
            deleteButton({treeId: row.treeId}).then(res => {
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
            this.$Message.info('取消删除');
          }
        });
      },
      handleEditButton(row, index) {
        this.editTreeId = row.treeId
        this.updateButtonShow = true
      },
      handleUpdateButton(index) {
        updateButton({
          treeId: this.editTreeId,
          treeCode: this.editTreeCode,
          treeName: this.editTreeName
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success(this.$t('tree.updateButtonSuccess'))
            this.editIndex = -1
            this.handleSearch()
          } else {
            this.$Message.error(this.$t('tree.updateButtonFail') + ',' + res.msg)
          }
        });
      },
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
        this.handleSearch();
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
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        let parentTreeId = this.parentTreeId
        const _this = this;
        queryTreeButtonList({
          current,
          pageSize,
          search,
          parentTreeId,
          orderKey,
          orderByValue
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success('查询成功')
            _this.total = res.obj.total
            _this.treeButtonData = res.obj.rows
          } else {
            this.$Message.error(res.msg)
          }
        })
      },
      handleAdd() {
        this.addTreeShow = true;
      },
      handleUpdate() {
        if (this.parentTreeId == 0) {
          this.$Message.warning('请选择需要修改的菜单节点');
          return;
        }
        this.updateTreeShow = true
      },
      handleDelete() {
        if (this.parentTreeId == 0) {
          this.$Message.warning('请选择需要删除的菜单节点');
          return;
        }
        this.$Modal.confirm({
          title: '删除菜单',
          content: '<p>是否删除当前的菜单节点</p>',
          onOk: () => {
            deleteTree({treeId: this.parentTreeId}).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.parentTreeId = 0;
                this.parentTreeName = '顶层节点';
                // 删除数据成功同时刷新grid
                this.initTree();
              } else {
                this.$Message.warning(res.msg);
              }
            });
          },
          onCancel: () => {
            this.$Message.info('取消了删除');
          }
        });
      },
      onSelectChange(data) {
        // 如果长度为0说明当前没有任何节点被选中
        if (data.length == 0) {
          this.parentTreeId = 0;
          this.parentTreeName = '顶层节点';
          this.hasChildren = false;
        } else {
          this.parentTreeId = data[0].treeId;
          this.parentTreeName = data[0].title;
          if (data[0].children.length == 0) {
            this.hasChildren = false;
          } else {
            this.hasChildren = true;
          }
        }
      },
      initTree() {
        const _this = this;
        getTreeList({}).then(res => {
          if (res.code == 200) {
            _this.roleTreeDate = res.obj;
          } else {
            this.$Message.error("加载菜单节点失败，请与管理员联系！")
          }
        });
      }
    },
    mounted() {
      // 初始化完成以后加载菜单数据
      this.initTree();
      this.tableHeight = window.innerHeight - this.$refs.treeButtonTable.$el.offsetTop - 335
    },
    watch: {
      parentTreeId() {
        this.handleSearch();
      }
    }
  }
</script>
