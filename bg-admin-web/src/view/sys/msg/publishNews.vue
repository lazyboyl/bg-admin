<template>
  <Modal v-model="show" title="发布消息" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="messageForm" :model="messageForm" :rules="messageFormRule">
      <FormItem label="消息标题" prop="title">
        <Input type="text" :maxlength=50 v-model="messageForm.title" placeholder="请输入消息标题"/>
      </FormItem>
      <FormItem label="消息内容" prop="content">
        <Input type="textarea" :rows="4" :maxlength=1000 v-model="messageForm.content" placeholder="请输入消息内容"/>
      </FormItem>
      <FormItem label="接收者" prop="targetUsers">
        <Transfer
          :data="userData"
          :render-format="userRender"
          :target-keys="messageForm.targetUsers"
          filterable
          @on-change="handleChange"></Transfer>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>
  import {
    queryUserList
  } from "../../../api/sys/user/user.api"

  import {
    publishNews
  } from '../../../api/sys/msg/msg.api'

  export default {
    name: 'publishNews',
    props: {
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        userData: [],
        messageForm: {
          title: '',
          content: '',
          targetUsers: []
        },
        messageFormRule: {
          title: [
            {required: true, message: '消息标题不能为空！', trigger: 'blur'},
            {type: 'string', max: 50, message: '消息标题最大长度不能超过50！', trigger: 'blur'}
          ],
          content: [
            {required: true, message: '消息内容不能为空！', trigger: 'blur'},
            {type: 'string', max: 200, message: '消息内容长度不能超过200！', trigger: 'blur'}
          ],
          targetUsers: [
            {required: true, message: '接收者不能为空！', trigger: 'change', type: "array"}
          ]
        }
      }
    },
    methods: {
      userRender(item) {
        return item.label
      },
      handleChange(newTargetKeys, direction, moveKeys) {
        this.messageForm.targetUsers = newTargetKeys;
      },
      ok() {
        this.$refs['messageForm'].validate((valid) => {
          if (valid) {
            publishNews({
              title: this.messageForm.title,
              content: this.messageForm.content,
              targetUsers: this.messageForm.targetUsers.join(",")
            }).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('handleSearch');
              } else {
                this.$Message.error(res.msg);
              }
            })
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        })
      },
      closeModal(val) {
        this.$emit('input', val);
      },
      initUserData() {
        queryUserList({
          current: 1,
          pageSize: 9999,
          search: '',
          orderKey: '',
          orderByValue: '',
          fullPath: ''
        }).then(res => {
          if (res.code == 200) {
            for (let i = 0; i < res.obj.rows.length; i++) {
              let user = res.obj.rows[i];
              user.label = user.loginAccount;
              user.key = user.loginAccount + '|' + user.userId;
              this.userData.push(user);
            }
          }
        })
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['messageForm'].resetFields();
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    },
    mounted() {
      this.initUserData()
    }
  }
</script>
