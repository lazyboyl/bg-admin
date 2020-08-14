<template>
  <Modal v-model="show" title="新增好友分组" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="addFriendGroupForm" :model="addFriendGroupForm" :rules="addFriendGroupFormRule" :label-width="80">
      <Row :gutter="16">
        <Col span="24">
          <FormItem label="分组名称" prop="friendGroupName">
            <Input type="text" :maxlength=10 v-model="addFriendGroupForm.friendGroupName" placeholder="请输入分组名称"/>
          </FormItem>
        </Col>
      </Row>
    </Form>
  </Modal>
</template>
<script>
    import {createFriendGroup} from '@/api/chat/friend/friend.api';
    export default {
        name: "addFriendGroup",
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
                addFriendGroupForm: {
                    friendGroupName: ''
                },
                addFriendGroupFormRule: {
                    friendGroupName: [
                        {required: true, message: '分组名称不能为空', trigger: 'blur'},
                        {type: 'string', max: 10, message: '分组名称不能大于10个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            ok() {
                this.$refs['addFriendGroupForm'].validate((valid) => {
                    if (valid) {
                        createFriendGroup(this.addFriendGroupForm).then(res => {
                            if (res.code == 200) {
                                this.$Message.success(res.msg);
                                // 提交表单数据成功则关闭当前的modal框
                                this.closeModal(false);
                                // 同时调用父页面的刷新页面的方法
                                this.$emit('operateFriendData',res.obj);
                            } else {
                                this.$Message.error( res.msg);
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
            }
        },
        watch: {
            value(val) {
                this.show = val;
            },
            show(val) {
                if (val) {
                    this.$refs['addFriendGroupForm'].resetFields();
                } else {// 反之则关闭页面
                    this.closeModal(val);
                }
            }
        }
    }
</script>
