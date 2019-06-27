<template>
  <Modal v-model="show" title="增加菜单" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="treeForm" :model="treeForm" :rules="treeFormRule">
      <FormItem label="父菜单名称">
        <Input type="text" v-model="treeForm.parentTreeName" disabled/>
      </FormItem>
      <FormItem label="菜单名称" prop="treeName">
        <Input type="text" :maxlength=50 v-model="treeForm.treeName" placeholder="请输入菜单名称"/>
      </FormItem>
      <FormItem label="菜单编码" prop="treeCode">
        <Input type="text" :maxlength=50 v-model="treeForm.treeCode" placeholder="请输入菜单编码"/>
      </FormItem>
      <FormItem label="菜单权限">
        <Input type="textarea" :rows="4" :maxlength=1000 v-model="treeForm.powerPath" placeholder="请输入菜单权限"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>
  import {checkTreeCode, addTree} from "../../../api/sys/tree/tree.api"


  export default {
    name: "addTree",
    props: {
      value: {
        type: Boolean,
        default: false
      },
      parentTreeId: {
        type: Number
      },
      parentTreeName: {
        type: String
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        treeForm: {
          treeName: '',
          treeCode: '',
          parentTreeId: '',
          parentTreeName: '',
          powerPath: ''
        },
        treeFormRule: {
          treeName: [
            {required: true, message: '菜单名称不能为空！', trigger: 'blur'},
            {type: 'string', max: 50, message: '菜单名称最大长度为50.', trigger: 'blur'}
          ],
          treeCode: [
            {required: true, message: '菜单编码不能为空！', trigger: 'blur'},
            {type: 'string', max: 50, message: '菜单编码最大长度为50.', trigger: 'blur'},
            {
              validator: this.checkTreeCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
      }
    },
    methods: {
      ok() {
        this.$refs['treeForm'].validate((valid) => {
          if (valid) {
            addTree(this.treeForm).then(res => {
              if (res.code == 200) {
                this.$Message.success('新增菜单节点成功');
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('reloadTree');
              } else {
                this.$Message.error( res.msg);
              }
            })
          } else {
            this.$Message.error('表单验证不通过！');
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      checkTreeCode() {
        let _this = this;
        return function (rule, value, callback) {
          let treeCode = value;
          checkTreeCode({treeCode: treeCode}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('菜单节点已经存在'));
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
          this.$refs['treeForm'].resetFields();
          this.treeForm.parentTreeId = this.parentTreeId;
          this.treeForm.parentTreeName = this.parentTreeName;
          this.treeForm.powerPath = '';
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    }
  }

</script>
