<template>
  <div>
    <Card title="日志查询">
      <div>
        <div style="display:inline-block;float:right;">
          <Input v-model="search" suffix="ios-search" placeholder="请输入需要查询的信息" style="width: auto"
                 :search=true @on-search="handleSearch"/>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table ref="behaviorTable" @on-sort-change="onSortChange"  :columns="columns" :data="behaviorData" :height="tableHeight">
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

  import {queryBehaviorLogList} from '../../../api/sys/behavior/behavior.api';

  export default {
    data(){
      return {
        search:'',
        key:'actionDate',
        order:'desc',
        total: 0,
        current: 1,
        pageSize: 10,
        columns:[
          {
            title: '请求方法',
            key: 'actionMethod',
            sortable: true
          },
          {
            title: '请求人',
            key: 'actionUser',
            sortable: true
          },
          {
            title: '请求时间',
            key: 'actionDate',
            sortable: true,
            render: (h, params) => {
              return h('div',
                this.formatDate(new Date(params.row.actionDate), 'yyyy/MM/dd hh:mm:ss')
              )
            }
          }
        ],
        behaviorData:[],
        tableHeight:200
      }
    },
    methods:{
      handleSearch(){
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        const _this = this;
        queryBehaviorLogList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success('获取日志列表成功')
            _this.total = res.obj.total
            _this.behaviorData = res.obj.rows
          } else {
            this.$Message.error('获取日志列表失败' + ',' + res.msg)
          }
        });
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
      // 初始化完成组件的时候执行以下的逻辑
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.behaviorTable.$el.offsetTop - 270
    }
  }

</script>
