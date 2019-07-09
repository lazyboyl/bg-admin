<template>
  <Modal v-model="show" title="新增用户" @on-ok="ok" :loading="loading" :mask-closable="false">
    <Form ref="userForm" :model="userForm" :rules="userFormRule">
      <FormItem label="用户账号" prop="loginAccount">
        <Input type="text" :maxlength=50 v-model="userForm.loginAccount"
               placeholder="请输入用户账号"/>
      </FormItem>
      <FormItem label="用户名称" prop="nickName">
        <Input type="text" :maxlength=50 v-model="userForm.nickName" placeholder="请输入用户名称"/>
      </FormItem>
      <FormItem label="用户角色" prop="roles">
        <Select v-model="userForm.roles" multiple placeholder="请选择用户角色">
          <Option v-for="item in roleDate" :value="item.roleId" :key="item.roleId">{{ item.roleName }}</Option>
        </Select>
      </FormItem>
      <FormItem label="所属组织" prop="orgIds"
                placeholder="请选择所属组织">
        <Cascader v-model="userForm.orgIds" :data="orgData" change-on-select filterable></Cascader>
      </FormItem>
      <FormItem label="联系地址" prop="pca">
        <al-cascader v-model="userForm.pca" level="2" @on-change="pcaChange"></al-cascader>
      </FormItem>
      <FormItem label="详细地址" prop="address">
        <Input type="textarea" :rows="4" :maxlength=100 v-model="userForm.address" placeholder="请输入详细地址"/>
      </FormItem>
      <FormItem label="头像地址" prop="headImg">
        <Row>
          <Col span="24">
            <baseImgUpload ref="headImg" :maxLength="1" idImageType="user"
                           @getUploadList="getHeadImgUploadList"></baseImgUpload>
          </Col>
        </Row>
      </FormItem>
    </Form>
  </Modal>
</template>
<script>
  import {getOrgCascader} from "../../../api/sys/org/org.api"
  import {
    loadAllRole,
    createUser,
    checkLoginAccount
  } from "../../../api/sys/user/user.api"

  import baseImgUpload from '../../../components/upload';

  export default {
    name: "addOrg",
    components: {
      baseImgUpload
    },
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
        userForm: {
          loginAccount: '',
          nickName: '',
          orgIds: [],
          roles: [],
          headImg: '',
          province: '',
          provinceName: '',
          city: '',
          cityName: '',
          area: '',
          areaName: '',
          address: '',
          pca: []
        },
        userFormRule: {
            loginAccount: [
              {required: true, message: '请输入用户账号', trigger: 'blur'},
              {type: 'string', max: 50, message: '请输入不超过50的用户账号', trigger: 'blur'},
              {
                validator: this.checkLoginAccount({
                  response: 'exist'
                }), trigger: 'blur'
              }
            ],
            nickName: [
              {required: true, message: '请输入用户名称', trigger: 'blur'},
              {type: 'string', max: 50, message: '请输入不超过50的用户名称', trigger: 'blur'}
            ],
            pca: [
              {required: true, message: '请选择联系地址', trigger: 'change', type: "array"}
            ],
            address:[
              {required: true, message: '请输入详细地址', trigger: 'blur'}
            ],
            roles: [
              {required: true, message: '请选择用户角色', trigger: 'change', type: "array"}
            ],
            orgIds: [
              {required: true, message: '请选择用户所属组织', trigger: 'change', type: "array"}
            ]
        },
        orgData: [],
        roleDate: []
      }
    },
    methods: {
      getHeadImgUploadList(val) {
        if (val.length > 0) {
          this.userForm.headImg = val[0].name;
        } else {
          this.userForm.headImg = '';
        }
      },
      ok() {
        this.$refs['userForm'].validate((valid) => {
          if (valid) {
            createUser({
              loginAccount: this.userForm.loginAccount,
              nickName: this.userForm.nickName,
              headImg: this.userForm.headImg,
              orgIds: this.userForm.orgIds.join(','),
              roles: this.userForm.roles.join(','),
              province: this.userForm.province,
              provinceName: this.userForm.provinceName,
              city: this.userForm.city,
              cityName: this.userForm.cityName,
              area: this.userForm.area,
              areaName: this.userForm.areaName,
              address: this.userForm.address
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
      checkLoginAccount() {
        let _this = this;
        return function (rule, value, callback) {
          let loginAccount = value;
          checkLoginAccount({loginAccount: loginAccount}).then(res => {
            if (res.obj.success == 'pass') {
              callback();
            } else {
              callback(new Error('账号已经存在'));
            }
          });
        };
      },
      closeModal(val) {
        this.$emit('input', val);
      },
      pcaChange(data) {
        this.userForm.province = data[0].code
        this.userForm.provinceName = data[0].name
        this.userForm.city = data[1].code
        this.userForm.cityName = data[1].name
        this.userForm.area = data[2].code
        this.userForm.areaName = data[2].name
      }
    },
    watch: {
      value(val) {
        this.show = val;
      },
      show(val) {
        //当重新显示增加数据的时候重置整个form表单
        if (val) {
          this.$refs['userForm'].resetFields();
          this.$refs['headImg'].cleanAll();
          getOrgCascader({}).then(res => {
            if (res.code == 200) {
              this.orgData = res.obj;
            } else {
              this.$Message.error(res.msg);
            }
          });
          loadAllRole({}).then(res => {
            if (res.code == 200) {
              this.roleDate = res.obj;
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
