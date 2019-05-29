<template>
  <Modal v-model="show" title="新增字典" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="dictForm" :model="dictForm" :rules="dictFormRule">
      <FormItem label="字典类型" prop="dictType">
        <Input type="text" :maxlength=50 v-model="dictForm.dictType" placeholder="请输入字典类型"/>
      </FormItem>
      <FormItem label="字典编码" prop="dictCode">
        <Input type="text" :maxlength=50 v-model="dictForm.dictCode" placeholder="请输入字典编码"/>
      </FormItem>
      <FormItem label="字典文本" prop="dictText">
        <Input type="text" :maxlength=50 v-model="dictForm.dictText" placeholder="请输入字典文本"/>
      </FormItem>
      <FormItem label="字典数值" prop="dictValue">
        <Input type="text" :maxlength=50 v-model="dictForm.dictValue" placeholder="请输入字典数值"/>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>

  import {addDict, checkTypeAndCode} from '../../../api/sys/dict/dict.api'

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
        dictForm: {
          dictType: '',
          dictCode: '',
          dictText: '',
          dictValue: ''
        },
        dictFormRule: {
          dictType: [
            {required: true, message: '字典类型不能为空', trigger: 'blur'},
            {type: 'string', max: 50, message: '字典类型不能大于50个字符', trigger: 'blur'}
          ],
          dictCode: [
            {required: true, message: '字典编码不能为空', trigger: 'blur'},
            {type: 'string', max: 50, message: '字典编码不能大于50个字符', trigger: 'blur'},
            {
              validator: this.check({
                response: 'exist'
              }), trigger: 'blur'
            }
          ],
          dictText: [
            {required: true, message: '字典文本不能为空', trigger: 'blur'},
            {type: 'string', max: 50, message: '字典文本不能大于50个字符', trigger: 'blur'}
          ],
          dictValue: [
            {required: true, message: '字典数值不能为空', trigger: 'blur'},
            {type: 'string', max: 50, message: '字典数值不能大于50个字符', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      ok() {
        this.$refs['dictForm'].validate((valid) => {
          if (valid) {
            addDict(this.dictForm).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                // 提交表单数据成功则关闭当前的modal框
                this.closeModal(false);
                // 同时调用父页面的刷新页面的方法
                this.$emit('handleSearch');
              } else {
                this.$Message.error( res.msg);
              }
            })
          } else {
            this.$Message.error('新增数据字典失败');
          }
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        });
      },
      check() {
        let _this = this;
        return function (rule, value, callback) {
          let dictType = _this.dictForm.dictType;
          let dictCode = value;
          checkTypeAndCode({dictType, dictCode}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error(_this.$t('dict.checkFail')));
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
        if (val) {
          this.$refs['dictForm'].resetFields();
        } else {// 反之则关闭页面
          this.closeModal(val);
        }
      }
    }
  }
</script>
