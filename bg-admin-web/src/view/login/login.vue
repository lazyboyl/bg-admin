<template>
  <div class="layout">
    <Layout>
      <Header class="layout-header" id="layout-header-scroll">
        <Menu mode="horizontal" theme="dark" active-name="1">
          <div class="layout-logo">
            <img height="50px" width="50px" src="../../assets/logo.png"/>
          </div>
          <div class="layout-nav">
            <language @on-lang-change="setLanguage" style="margin-right: 10px;" :lang="local"/>
          </div>
          <div class="layout-nav">
            <MenuItem name="1" style="font-size: 30px;">
              欢迎使用xxx系统
            </MenuItem>
          </div>
        </Menu>
      </Header>
      <Content :style="{ background: '#fff', minHeight: '500px'}">
        <div style="float: right;margin: 100px 100px 0">
          <Card title="欢迎登录XX系统">
            <Form ref="loginForm" :model="loginForm" :rules="loginFormRule">
              <div>
                <FormItem prop="loginAccount">
                  账号：<Input v-model="loginForm.loginAccount" prefix="ios-contact"
                            :placeholder="$t('login.loginAccount')"
                            style="width: 200px;"/>
                </FormItem>
                <FormItem prop="loginPassword">
                  密码：<Input v-model="loginForm.loginPassword" prefix="ios-compass" type="password"
                            :placeholder="$t('login.loginPassword')" style="width: 200px;"/>
                </FormItem>
              </div>
            </Form>
            <div style="margin-top: 20px;">
              <Button type="primary" @click="loginSystem" :long=true>登录</Button>
            </div>
          </Card>
        </div>
      </Content>
    </Layout>
  </div>
</template>
<script>
  import Language from '../../components/language';
  import { login } from '../../api/sys/user/user.api';

  export default {
    components: {
      Language
    },
    data() {
      return {
        local: localStorage.getItem("lang"),
        loginForm: {
          loginAccount: '',
          loginPassword: ''
        },
        loginFormRule: {
          loginAccount: [
            {required: true, message: '请输入账号', trigger: 'blur'},
            {type: 'string', max: 30, message: '账号允许输入最大长度为30个字符', trigger: 'blur'}
          ],
          loginPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {type: 'string', max: 50, message: '密码允许输入最大长度为50个字符', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      loginSystem() {
        this.$refs['loginForm'].validate((valid) => {
          if (valid) {
            login(this.loginForm).then(res=>{
              if(res.code==200){
                this.$router.push({
                  name: 'main'
                })
              }else{
                this.$Message.error('账号密码错误！');
              }
            })

          }
        })
      },
      setLanguage(lang) {
        this.local = lang
        localStorage.setItem('lang', lang)
      }
    },
    mounted() {
    }
  }
</script>
<style scoped>

  .layout-header {
    position: relative;
    z-index: 999;
    height: 60px;
  }

  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }

  .layout-nav {
    width: auto;
    float: right;
    margin: 0 auto;
    margin-right: 20px;
  }

  .layout-logo {
    width: 100px;
    height: 30px;
    border-radius: 10px;
    float: left;
    position: relative;
    left: 20px;
    top: 5px;
  }
</style>
