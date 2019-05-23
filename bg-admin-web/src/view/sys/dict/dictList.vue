<template>
  <div>
    <Card title="字典管理">
      <div>
        <div style="display:inline-block;float:left;">
          <Button type="success" @click="addDict">+新增字典</Button>
        </div>
        <div style="display:inline-block;float:right;">
          <Input v-model="search" suffix="ios-search" placeholder="请输入查询信息" style="width: auto"
                 :search=true @on-search="handleSearch"/>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table ref="dictTable" @on-sort-change="onSortChange"  :columns="columns" :data="dictData" :height="tableHeight">
          <template slot-scope="{ row, index }" slot="dictCode">
            <Input type="text" v-model="editDictCode" v-if="editIndex === index"/>
            <span v-else>{{ row.dictCode }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="dictText">
            <Input type="text" v-model="editDictText" v-if="editIndex === index"/>
            <span v-else>{{ row.dictText }}</span>
          </template>

          <template slot-scope="{ row, index }" slot="dictValue">
            <Input type="text" v-model="editDictValue" v-if="editIndex === index"/>
            <span v-else>{{ row.dictValue}}</span>
          </template>

          <template slot-scope="{ row, index }" slot="action">
            <div v-if="editIndex === index">
              <Button type="success" @click="handleUpdate(index)">确定</Button>
              <Button type="error" @click="editIndex = -1">取消</Button>
            </div>
            <div v-else>
              <Button type="primary" @click="handleEdit(row, index)" size="small">编辑</Button>
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

  import { deleteDict, updateDict, queryDictList} from '../../../api/sys/dict/dict.api'

  export default {
    data() {
      return {
        search: '',
        dictData: [],
        columns: [
          {
            title: '字典类型',
            key: 'dictType',
            sortable: true
          },
          {
            title: '字典编码',
            slot: 'dictCode',
            key: 'dictCode',
            sortable: true
          },
          {
            title: '字典文本',
            slot: 'dictText',
            key: 'dictText',
            sortable: true
          },
          {
            title: '字典数值',
            slot: 'dictValue',
            key: 'dictValue',
            sortable: true
          },
          {
            title: '操作',
            slot: 'action'
          }
        ],
        key:'dictType',
        order:'desc',
        editIndex: -1,  // 当前聚焦的输入框的行数
        editId: '',
        editDictCode: '',
        editDictText: '',
        editDictValue: '',
        tableHeight:200,
        total: 0,
        current: 1,
        pageSize: 10
      }
    },
    methods: {
      addDict() {
        console.log('增加字典')
      },
      changePage(current) {
        this.current = current;
        this.handleSearch();
      },
      changePageSize(pageSize) {// 改变每页记录的条数
        this.pageSize = pageSize;
        this.handleSearch();
      },
      handleSearch(){
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        const _this = this;
        queryDictList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success('新增数据字典成功')
            _this.total = res.obj.total
            _this.dictData = res.obj.rows
          } else {
            this.$Message.error(  '新增数据字典失败,' + res.msg)
          }
        });
      },
      handleUpdate(index){
        updateDict({
          id: this.editId,
          dictValue: this.editDictValue,
          dictText: this.editDictText,
          dictCode: this.editDictCode
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success('更新字典数据成功')
            this.editIndex = -1
            this.handleSearch()
          } else {
            this.$Message.error( '更新字典数失败,' + res.msg)
          }
        });
      },
      handleEdit(row, index){
        this.editDictCode = row.dictCode
        this.editDictText = row.dictText
        this.editDictValue = row.dictValue
        this.editId = row.id
        this.editIndex = index
      },
      handleDelete(row, index){
        this.$Modal.confirm({
          title: '提示',
          content: '<p>是否删除字典数据？</p>',
          onOk: () => {
            deleteDict({id: row.id}).then(res => {
              if (res.code == 200) {
                this.$Message.success('字典数据删除成功');
                // 删除数据成功同时刷新grid
                this.handleSearch();
              } else {
                this.$Message.warning('字典数据删除失败');
              }
            });
          },
          onCancel: () => {
            this.$Message.info('取消删除请求');
          }
        });
      },
      onSortChange(sort){
        console.log(sort.key+'-'+sort.order)
        if(sort.order=='normal'){
          this.order = '';
        }else{
          this.key = sort.key;
          this.order = sort.order;
        }
        this.handleSearch();
      }
    },
    mounted() {
      // 初始化完成组件的时候执行以下的逻辑
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.dictTable.$el.offsetTop - 270
    }
  }

</script>
