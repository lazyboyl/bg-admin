<template>
  <div class="layout">
    <Layout>
      <!--  Header 表示头部的位置-->
      <Header id="layout-header-scroll">
        <Menu mode="horizontal" theme="dark" active-name="1">
          <div class="layout-logo">
            <img height="50px" width="50px" src="../../assets/logo.png"/>
          </div>
          <div class="layout-nav" >
            <Poptip
              placement="bottom-end"
              :transfer=true
              ref="queryMsgPop"
            >
              <Badge dot :offset="[21,21]" :count="msgCount">
                <a href="javascript:void(0)" >
                  <Icon  type="ios-chatbubbles-outline" size="36"></Icon>
                </a>
              </Badge>
              <div slot="content">
                <template v-for="(msg,index) in msgList">
                  <Divider v-if="index==0" />
                  <p @click="showMsg(msg.title,msg.content,index,msg.targetMessageList[0].targetMessageId)">{{msg.content}}</p>
                  <Divider />
                </template>
              </div>
            </Poptip>
          </div>
          <div class="layout-nav">
            <Dropdown @on-click="userAction">
              <a href="javascript:void(0)" style="color: white">
                {{this.nickName}}
              </a>
              <DropdownMenu slot="list">
                <DropdownItem name="regPass">修改密码</DropdownItem>
                <DropdownItem name="loginOut" divided>退出登录</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
          <div class="layout-nav">
            <language @on-lang-change="setLanguage" style="margin-right: 10px;" :lang="local"/>
          </div>
          <div class="layout-nav">
            <template v-for="item in menuList">
              <Submenu :name="item.meta.code" v-if="item.children.length>0">
                <template slot="title">
                  <Icon :type="item.meta.icon"/>
                  {{item.meta.title}}
                </template>
                <template v-for="childrenItem in item.children">
                  <MenuItem :name=childrenItem.meta.code :to="item.path+'/'+childrenItem.path">
                    <Icon :type=childrenItem.meta.icon>
                    </Icon>
                    {{childrenItem.meta.title}}
                  </MenuItem>
                </template>
              </Submenu>
              <MenuItem :name="item.meta.code" v-else>
                <Icon :type="item.meta.icon"/>
                {{item.meta.title}}
              </MenuItem>
            </template>
          </div>
        </Menu>
      </Header>
      <!-- 此处表示的是左侧的菜单栏的布局 -->
      <Layout>
        <Sider hide-trigger :style="{background: '#fff'}">
          <Menu active-name="1-2" theme="light" width="auto" :open-names="['system-manage']">
            <template v-for="item in menuList">
              <Submenu :name=item.meta.code>
                <template slot="title">
                  <Icon :type=item.meta.icon></Icon>
                  {{item.meta.title}}
                </template>
                <template v-for="childrenItem in item.children">
                  <MenuItem :name=childrenItem.meta.code :to="item.path+'/'+childrenItem.path">
                    <Icon :type=childrenItem.meta.icon>
                    </Icon>
                    {{childrenItem.meta.title}}
                  </MenuItem>
                </template>
              </Submenu>
            </template>
          </Menu>
        </Sider>
        <Layout :style="{padding: '0 24px 24px'}">
          <!--  此处是面包屑导航条 -->
          <Breadcrumb :style="{margin: '24px 0'}">
            <BreadcrumbItem>
              <Icon type="ios-home-outline"></Icon>
              首页
            </BreadcrumbItem>
            <BreadcrumbItem v-for="item in breadCrumbList" v-bind:key="item.name" v-if="item.meta && item.meta.title">
              <Icon :type="item.icon"></Icon>
              {{showBreadcrumbItem(item)}}
            </BreadcrumbItem>
          </Breadcrumb>
          <!-- 此处存放的是文本内容的区域 -->
          <Content :style="{padding: '24px', minHeight: '280px', background: '#fff'}">
            <router-view/>
          </Content>
        </Layout>
      </Layout>
    </Layout>
    <changePassword v-model="showChangePassword"></changePassword>
  </div>
</template>
<script>
  import Language from '../../components/language';
  import {mapMutations, mapActions} from 'vuex';
  import changePassword from './changePassword';
  import {queryUserMsg,readMsg} from '../../api/sys/msg/msg.api';
  import io from 'socket.io-client'

  export default {
    components: {
      Language,
      changePassword
    },
    data() {
      return {
        local: localStorage.getItem("lang"),
        showChangePassword: false,
        msgCount: 0,
        msgList:[]
      }
    },
    methods: {
      ...mapMutations([
        'setBreadCrumb'
      ]),
      ...mapActions([
        'handleLogOut'
      ]),
      /**
       * 顶部跟随着滚动条的变化而滚动
       */
      handleScroll() {
        let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
        if (scrollTop >= 60) {
          document.querySelector('#layout-header-scroll').style.top = scrollTop + 'px';
        } else {
          document.querySelector('#layout-header-scroll').style.top = '0px';
        }
      },
      userAction(name) {
        // 实现退出登录
        if (name == 'loginOut') {
          this.handleLogOut();
          this.turnToView('login');
          // 实现修改密码
        } else if (name == 'regPass') {
          this.showChangePassword = true;
        }
      },
      setLanguage(lang) {
        this.local = lang
        localStorage.setItem('lang', lang)
      },
      showBreadcrumbItem(item) {
        return (item.meta && item.meta.title) || item.name
      },
      turnToView(name) {
        this.$router.push({
          name: name
        })
      },
      showMsg(title,content,index,targetMessageId){
        readMsg({targetMessageId}).then(res=>{
          if (res.code == 200) {
            this.$Modal.info({title,content});
            this.msgList.splice(index,index+1)
          } else {
            this.$Message.error(res.msg)
          }
        });
      },
      initMsg() {
        queryUserMsg({}).then(res => {
          if (res.code == 200) {
            this.msgCount = res.obj.length;
            this.msgList = res.obj;
          } else {
            this.$Message.error(res.msg)
          }
        })
      },
      initSocketIo(socketToken,refreshToken){
        let _this = this;
        let opts = {
          query: 'refreshToken=' + refreshToken + '&socketToken=' + socketToken
        };
        let socket = io.connect(this.$runConfig.runConfig.socketUrl,opts);
        socket.on('connect', function () {
          console.log("连接成功");
        });
        socket.on('push_event', function (data) {
          console.log(data);
          _this.initMsg();
          _this.$Notice.info({
            title: '消息通知',
            desc: data.content
          });
        });
        socket.on('disconnect', function () {
          console.log("已经下线");
        });
      }
    },
    watch: {
      '$route'(newRoute) {
        this.setBreadCrumb(newRoute.matched)
      }
    },
    computed: {
      breadCrumbList() {
        return this.$store.state.app.breadCrumbList
      },
      menuList() {
        return this.$store.getters.menuList;
      },
      nickName() {
        return this.$store.getters.nickName;
      }
    },
    mounted() {
      /**
       * 监听滚动条的滚动事件
       */
      window.addEventListener('scroll', this.handleScroll)
      /**
       * 开启socket的监听
       */
      this.initSocketIo(this.$uuid(), localStorage.getItem("refreshToken"));
      this.initMsg();
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

  .layout-logo {
    width: 100px;
    height: 30px;
    border-radius: 10px;
    float: left;
    position: relative;
    left: 20px;
    top: 5px;
  }

  .layout-nav {
    width: auto;
    float: right;
    margin: 0 auto;
    margin-right: 20px;
  }
</style>
