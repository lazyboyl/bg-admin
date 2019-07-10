<template>
  <div>
    <Card title="消息管理">
      <div>
        <div style="display:inline-block;float:left;">
          <Button type="success" @click="handleAdd">+发布消息</Button>
        </div>
        <div style="display:inline-block;float:right;">
          <Input v-model="search" suffix="ios-search" placeholder="请输入相应的查询信息" style="width: auto"
                 :search=true @on-search="handleSearch"/>
        </div>
      </div>
      <div style="clear: both;"></div>
      <div style="margin-top: 10px;">
        <Table ref="msgTable" @on-sort-change="onSortChange" :columns="columns" :data="msgData" :height="tableHeight">
        </Table>
      </div>
      <div style="margin-top: 10px;">
        <Page show-elevator show-sizer show-total :total="total" :current="current"
              :page-size="pageSize" @on-change="changePage" @on-page-size-change="changePageSize"/>
      </div>
    </Card>
    <publishNews v-model="addShow" v-on:handleSearch="handleSearch"></publishNews>
  </div>
</template>
<script>
  import {queryMessageList} from '../../../api/sys/msg/msg.api'
  import expandReceiverList from './ExpandReceiverList'
  import publishNews from './publishNews'

  export default {
    name: 'msgList',
    components:{
      expandReceiverList,
      publishNews
    },
    data() {
      return {
        search: '',
        addShow: false,
        msgData: [],
        tableHeight: 200,
        key: 'crtDate',
        order: 'desc',
        total: 0,
        current: 1,
        pageSize: 10,
        columns: [
          {
            type: 'expand',
            width: 50,
            render: (h, params) => {
              return h(expandReceiverList, {
                props: {
                  targetMessageList: params.row.targetMessageList
                }
              })
            }
          },
          {
            title: '消息标题',
            key: 'title',
            tooltip: true,
            sortable: true
          },
          {
            title: '消息内容',
            key: 'content',
            tooltip: true,
            sortable: true
          },
          {
            title: '消息类型',
            key: 'type',
            sortable: true,
            render: (h, params) => {
              if (params.row.type == '1') {
                return h('div',
                  '系统消息'
                )
              } else {
                return h('div',
                  '其他消息'
                )
              }
            }
          }
        ]
      }
    },
    methods: {
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
      handleAdd(){
        this.addShow=true
      },
      handleSearch() {
        let current = this.current
        let pageSize = this.pageSize
        let search = this.search
        let orderKey = this.key
        let orderByValue = this.order
        const _this = this;
        queryMessageList({
          current,
          pageSize,
          search,
          orderKey,
          orderByValue,
        }).then(res => {
          if (res.code == 200) {
            this.$Message.success('数据查询成功！')
            _this.total = res.obj.total
            _this.msgData = res.obj.rows
          } else {
            this.$Message.error('数据查询失败，失败原因：' + res.msg)
          }
        });
      }
    },
    mounted() {
      // 初始化完成组件的时候执行以下的逻辑
      this.handleSearch();
      this.tableHeight = window.innerHeight - this.$refs.msgTable.$el.offsetTop - 270
    }
  }
</script>
