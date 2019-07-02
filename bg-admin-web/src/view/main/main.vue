<template>
  <div class="layout">
    <Layout>
      <!--  Header 表示头部的位置-->
      <Header id="layout-header-scroll">
        <Menu mode="horizontal" theme="dark" active-name="1" >
          <div class="layout-logo">
            <img height="50px" width="50px" src="../../assets/logo.png"/>
          </div>
          <div class="layout-nav">
            <language @on-lang-change="setLanguage" style="margin-right: 10px;" :lang="local"/>
          </div>
          <div class="layout-nav">
            <MenuItem name="1">
              <Icon type="ios-paper"/>
              内容管理
            </MenuItem>
            <MenuItem name="2">
              <Icon type="ios-people"/>
              用户管理
            </MenuItem>
            <Submenu name="3">
              <template slot="title">
                <Icon type="ios-stats"/>
                统计分析
              </template>
              <MenuGroup title="使用">
                <MenuItem name="3-1">新增和启动</MenuItem>
                <MenuItem name="3-2">活跃分析</MenuItem>
                <MenuItem name="3-3">时段分析</MenuItem>
              </MenuGroup>
              <MenuGroup title="留存">
                <MenuItem name="3-4">用户留存</MenuItem>
                <MenuItem name="3-5">流失用户</MenuItem>
              </MenuGroup>
            </Submenu>
            <MenuItem name="4">
              <Icon type="ios-construct"/>
              综合设置
            </MenuItem>
          </div>
        </Menu>
      </Header>
      <!-- 此处表示的是左侧的菜单栏的布局 -->
      <Layout>
        <Sider hide-trigger :style="{background: '#fff'}">
          <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
            <Submenu name="1">
              <template slot="title">
                <Icon type="ios-navigate"></Icon>
                系统管理
              </template>
              <MenuItem name="1-1" to="/sys/dictList">数据字典</MenuItem>
              <MenuItem name="1-2" to="/sys/treeList">菜单管理</MenuItem>
              <MenuItem name="1-3" to="/sys/roleList">角色管理</MenuItem>
              <MenuItem name="1-4" to="/sys/orgList">用户组织</MenuItem>
            </Submenu>
            <Submenu name="2">
              <template slot="title">
                <Icon type="ios-keypad"></Icon>
                Item 2
              </template>
              <MenuItem name="2-1">Option 1</MenuItem>
              <MenuItem name="2-2">Option 2</MenuItem>
            </Submenu>
            <Submenu name="3">
              <template slot="title">
                <Icon type="ios-analytics"></Icon>
                Item 3
              </template>
              <MenuItem name="3-1">Option 1</MenuItem>
              <MenuItem name="3-2">Option 2</MenuItem>
            </Submenu>
          </Menu>
        </Sider>
        <Layout :style="{padding: '0 24px 24px'}">
          <!--  此处是面包屑导航条 -->
          <Breadcrumb :style="{margin: '24px 0'}">
            <BreadcrumbItem>Home</BreadcrumbItem>
            <BreadcrumbItem>Components</BreadcrumbItem>
            <BreadcrumbItem>Layout</BreadcrumbItem>
          </Breadcrumb>
          <!-- 此处存放的是文本内容的区域 -->
          <Content :style="{padding: '24px', minHeight: '280px', background: '#fff'}">
            <router-view/>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>
<script>
  import Language from '../../components/language';

  export default {
    components:{
      Language
    },
    data() {
      return {
        local:localStorage.getItem("lang")
      }
    },
    methods:{
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
      setLanguage(lang) {
        this.local = lang
        localStorage.setItem('lang',lang)
      }
    },
    mounted() {
      /**
       * 监听滚动条的滚动事件
       */
      window.addEventListener('scroll', this.handleScroll)
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
