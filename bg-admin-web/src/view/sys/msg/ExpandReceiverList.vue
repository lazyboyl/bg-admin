<template>
  <Row>
    <Col span="24">
      <Table :columns="expandColumns" :data="expandData" stripe border >
      </Table>
    </Col>
  </Row>
</template>
<script>
  export default {
    name: 'expandReceiveList',
    props: {
      targetMessageList: {
        type: Array,
        default: []
      },
    },
    data() {
      return {
        expandData: this.targetMessageList,
        expandColumns: [
          {
            title: '收取人',
            key: 'targetName'
          },
          {
            title: '阅读状态',
            key: 'state',
            render: (h, params) => {
              if (params.row.state == '1') {
                return h('div',
                  '未读'
                )
              } else {
                return h('div',
                  '已读'
                )
              }
            }
          },
          {
            title: '发送时间',
            key: 'sendTime',
            render: (h, params) => {
              if(params.row.sendTime){
                return h('div',
                  this.formatDate(new Date(params.row.sendTime), 'yyyy/MM/dd hh:mm:ss')
                )
              }else{
                return h('div',
                  ''
                )
              }
            }
          },
          {
            title: '查看时间',
            key: 'readeTime',
            render: (h, params) => {
              if(params.row.readeTime){
                return h('div',
                  this.formatDate(new Date(params.row.readeTime), 'yyyy/MM/dd hh:mm:ss')
                )
              }else{
                return h('div',
                  ''
                )
              }
            }
          },
          {
            title: '是否删除',
            key: 'removeState',
            render: (h, params) => {
              if (params.row.removeState == '0') {
                return h('div',
                  '已删除'
                )
              } else {
                return h('div',
                  '未删除'
                )
              }
            }
          }
        ]
      }
    }
  }
</script>
