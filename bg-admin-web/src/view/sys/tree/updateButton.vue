<template>
  <Modal v-model="show" title="修改按钮" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="buttonForm" :model="buttonForm" :rules="buttonFormRule">
      <FormItem label="父菜单节">
        <Input type="text" v-model="buttonForm.parentTreeName" disabled/>
      </FormItem>
      <FormItem label="按钮名称" prop="treeName">
        <Input type="text" :maxlength=50 v-model="buttonForm.treeName" placeholder="请输入按钮名称"/>
      </FormItem>
      <FormItem label="按钮编码" prop="treeCode">
        <Input type="text" :maxlength=50 v-model="buttonForm.treeCode" placeholder="请输入按钮编码"/>
      </FormItem>
      <FormItem label="按钮权限">
        <Input type="textarea" :rows="4" :maxlength=1000 v-model="buttonForm.powerPath"
               placeholder="请输入按钮权限"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {checkTreeCode, updateButton,getTreeByTreeId} from "../../../api/sys/tree/tree.api"

  export default {
    name: "addButton",
    props: {
      value: {
        type: Boolean,
        default: false
      },
      treeId: {
        type: Number
      }
    },
    data() {
      return {
        show: this.value,
        loading: true,
        buttonForm: {
          treeName: '',
          treeCode: '',
          parentTreeId: '',
          parentTreeName: '',
          powerPath: ''
        },
        buttonFormRule: this.getButtonFormRule()
      }
    },
    methods: {
      ok() {
        this.$refs['buttonForm'].validate((valid) => {
          if (valid) {
            updateButton(this.buttonForm).then(res => {
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
      checkButtonCode() {
        let _this = this;
        return function (rule, value, callback) {
          let buttonCode = value;
          checkTreeCode({treeCode: buttonCode,treeId:_this.treeId}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('按钮已经存在'));
            }
          });
        };
      },
      getButtonFormRule() {
        return {
          treeName: [
            {required: true, message: '请输入按钮名称', trigger: 'blur'},
            {type: 'string', max: 50, message: '按钮名称长度不能大于50', trigger: 'blur'}
          ],
          treeCode: [
            {required: true, message: '请输入按钮编码', trigger: 'blur'},
            {type: 'string', max: 50, message: '按钮编码长度不能大于50', trigger: 'blur'},
            {
              validator: this.checkButtonCode({
                response: 'exist'
              }), trigger: 'blur'
            }
          ]
        }
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
          this.$refs['buttonForm'].resetFields();
          getTreeByTreeId({treeId:this.treeId}).then(res=>{
            if(res.code==200){
              this.buttonForm = res.obj;
            }else{
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
