<template>
  <Modal v-model="show" title="修改角色" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="roleForm" :model="roleForm" :rules="roleFormRule">
      <Row>
        <Col span="11">
          <FormItem label="角色名称" prop="roleName">
            <Input type="text" :maxlength=50 v-model="roleForm.roleName" placeholder="请输入角色名称"/>
          </FormItem>
          <FormItem label="角色编码" prop="roleCode">
            <Input type="text" :maxlength=50 v-model="roleForm.roleCode" placeholder="请输入角色编码"/>
          </FormItem>
        </Col>
        <Col span="2">
        </Col>
        <Col span="11">
          <Tree :data="roleForm.roleTrees" show-checkbox ref="roleTree"></Tree>
        </Col>
      </Row>

    </Form>
  </Modal>
</template>
<script>

  import {updateRole, checkRoleCodeAndName, getRoleByRoleId} from '../../../api/sys/role/role.api'

  export default {
    name: "editRole",
    props: {
      roleId: {
        type: String
      },
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        roleForm: {
          roleId: '',
          roleName: '',
          roleCode: '',
          roleTrees: []
        },
        roleFormRule: {
          roleName: [
            {required: true, message: '请输入角色名称', trigger: 'blur'},
            {type: 'string', max: 50, message: '角色名称最大长度为50', trigger: 'blur'},
            {
              validator: this.checkRoleName({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          roleCode: [
            {required: true, message: '请输入角色编码', trigger: 'blur'},
            {type: 'string', max: 50, message: '角色编码最大长度为50', trigger: 'blur'},
            {
              validator: this.checkRoleCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      }
    },
    methods: {
      checkRoleCode() {
        let _this = this;
        return function (rule, value, callback) {
          let roleCode = value;
          let roleId = _this.roleForm.roleId
          checkRoleCodeAndName({roleCode, roleId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('角色编码重复'));
            }
          });
        }
      },
      checkRoleName() {
        let _this = this;
        return function (rule, value, callback) {
          let roleName = value;
          let roleId = _this.roleForm.roleId
          checkRoleCodeAndName({roleName, roleId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('角色名称重复'));
            }
          });
        }
      },
      ok() {
        this.$refs['roleForm'].validate((valid) => {
          if (valid) {
            updateRole({
              roleId: this.roleForm.roleId,
              roleName: this.roleForm.roleName,
              roleCode: this.roleForm.roleCode,
              roleTrees: JSON.stringify(this.roleForm.roleTrees)
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
          } else {
            this.$Message.error('表单验证失败');
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
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
          this.$refs['roleForm'].resetFields();
          let roleId = this.roleId;
          getRoleByRoleId({roleId: roleId}).then(res => {
            if (res.code == 200) {
              this.roleForm = res.obj;
            } else {
              this.$Message.success(res.msg);
              this.closeModal(false);
            }
          });
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    }
  }
</script>
