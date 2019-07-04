<template>
  <Modal v-model="show" title="修改密码" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="changePasswordForm" :model="changePasswordForm" :rules="changePasswordFormRule">
      <FormItem label="当前密码" prop="oldPassword">
        <Input type="password" :maxlength=50 v-model="changePasswordForm.oldPassword" placeholder="请输入当前密码"/>
      </FormItem>
      <FormItem label="新密码" prop="newPassword">
        <Input type="password" :maxlength=50 v-model="changePasswordForm.newPassword" placeholder="请输入新密码"/>
      </FormItem>
      <FormItem label="新密码二次确认" prop="checkNewPassword">
        <Input type="password" :maxlength=50 v-model="changePasswordForm.checkNewPassword" placeholder="请再次输入上面的密码"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {changePassword} from '../../api/sys/user/user.api'

  export default {
    name: "addDict",
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
        changePasswordForm: {
          oldPassword: '',
          newPassword: '',
          checkNewPassword: ''
        },
        changePasswordFormRule: {
          oldPassword: [
            {required: true, message: '请输入旧的密码', trigger: 'blur'},
            {type: 'string', max: 50, min: 6, message: '密码最小长度为6', trigger: 'blur'}
          ],
          newPassword: [
            {required: true, message: '请输入新密码', trigger: 'blur'},
            {type: 'string', max: 50, min: 6, message: '密码最小长度为6', trigger: 'blur'}
          ],
          checkNewPassword:[
            {required: true, message: '请再次输入密码', trigger: 'blur'},
            {type: 'string', max: 50, min: 6, message: '密码最小长度为6', trigger: 'blur'},
            {
              validator:this.validatorPassword({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      }
    },
    methods: {
      ok() {
        this.$refs['changePasswordForm'].validate((valid) => {
          if (valid) {
            changePassword(this.changePasswordForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
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
      },
      validatorPassword(){
        let _this = this;
        return function (rule, value, callback) {
          let newPassword = _this.changePasswordForm.newPassword;
          let checkNewPassword = _this.changePasswordForm.checkNewPassword;
          if(newPassword!=''&&checkNewPassword!=''){
              if(newPassword!=checkNewPassword){
                callback(new Error("两次输入的密码不匹配"));
              }else{
                callback();
              }
          }else{
            callback();
          }
        }
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['changePasswordForm'].resetFields();
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    }
  }

</script>
