<template>
  <Modal v-model="show" title="修改组织" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="orgForm" :model="orgForm" :rules="orgFormRule">
      <FormItem label="父组织架构名称">
        <Input type="text" v-model="orgForm.parentOrgName" disabled/>
      </FormItem>
      <FormItem label="组织架构名称" prop="orgName">
        <Input type="text" :maxlength=50 v-model="orgForm.orgName" placeholder="请输入组织架构名称"/>
      </FormItem>
      <FormItem label="组织架构编码" prop="orgCode">
        <Input type="text" :maxlength=50 v-model="orgForm.orgCode" placeholder="请输入组织架构编码"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {checkOrgNameAndCode, getOrgByOrgId, updateOrg} from "../../../api/sys/org/org.api"

  export default {
    name: "updateOrg",
    props: {
      value: {
        type: Boolean,
        default: false
      },
      orgId: {
        type: Number
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        orgForm: {
          orgId:0,
          orgName: '',
          orgCode: '',
          parentOrgId: 0,
          parentOrgName: '顶层组织架构'
        },
        orgFormRule: {
          orgName: [
            {required: true, message: '请输入组织架构名称', trigger: 'blur'},
            {type: 'string', max: 50, message: '请输入不超过50长度的组织架构名称', trigger: 'blur'},
            {
              validator: this.checkOrgName({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          orgCode: [
            {required: true, message: '请输入组织架构编码', trigger: 'blur'},
            {type: 'string', max: 50, message: '请输入不超过50长度的组织架构名称', trigger: 'blur'},
            {
              validator: this.checkOrgCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      }
    },
    methods: {
      ok() {
        this.$refs['orgForm'].validate((valid) => {
          if (valid) {
            updateOrg(this.orgForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('reloadTree');
              } else {
                this.$Message.error(res.msg);
              }
            })
          } else {
            this.$Message.error('表单验证不通过');
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      checkOrgCode() {
        let _this = this;
        return function (rule, value, callback) {
          let orgId = _this.orgId;
          let orgCode = value;
          checkOrgNameAndCode({orgCode: orgCode, orgId: orgId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('组织编码已经存在'));
            }
          });
        };
      },
      checkOrgName() {
        let _this = this;
        return function (rule, value, callback) {
          let orgId = _this.orgId
          let orgName = value;
          checkOrgNameAndCode({orgName: orgName, orgId: orgId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('组织架构名称已经存在'));
            }
          });
        };
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
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['orgForm'].resetFields();
          getOrgByOrgId({orgId: this.orgId}).then(res => {
            if (res.code == 200) {
              this.orgForm = res.obj;
            } else {
              this.$Message.error(res.msg);
            }
          });
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    }
  }

</script>
