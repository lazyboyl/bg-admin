<template>
  <div class="layout">
    <Layout>
      <Sider hide-trigger :style="{background: '#fff'}" width="260" >
        <Tabs style="min-height: 300px;">
          <TabPane label="消息" icon="logo-apple">
            <List size="small" >
              <ListItem >
                <ListItemMeta avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar" title="spring爱好交流群" description="技术大神在吗？" />
                <template slot="extra">
                  <div style="display: inline-block;">10:20</div>
                  <Badge :count="3" type="normal">
                  </Badge>
                </template>
              </ListItem>
              <ListItem>
                <ListItemMeta avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar" title="决战MAN" description="无聊死了" />
                <template slot="extra">
                  <div style="display: inline-block;">16:20</div>
                  <Badge :count="3" type="normal">
                  </Badge>
                </template>
              </ListItem>
              <ListItem>
                <ListItemMeta avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar" title="大伟哥" description="快来玩LOL" />
                <template slot="extra">
                  <div style="display: inline-block;">20:20</div>
                  <Badge :count="3" type="normal">
                  </Badge>
                </template>
              </ListItem>
            </List>
          </TabPane>
          <TabPane label="联系人" icon="logo-windows">
            <div>
              <Button type="text" size="small">好友</Button>
              <Button type="text" size="small">群聊</Button>
              <Button type="text" size="small" style="float: right;" icon="md-add"></Button>
            </div>
            <div style="margin-top: 5px;margin-left: 5px;">
              <Tree :data="friendData"></Tree>
            </div>
          </TabPane>
        </Tabs>
      </Sider>
      <Tabs style="width:100%;" closable type="card">
        <TabPane label="老三" icon="logo-apple">
          <Layout>
            <Content style="margin-left: 20px;">
              <Scroll>
                <List>
                  <li style="text-align: center;">
                    <a href="javascript:void(0)">查看更多消息</a>
                  </li>
                  <ChatListItem
                    avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                    talkDate="2020/08/08 22:10:30"
                    talkContent="你好啊，在做什么呢？"></ChatListItem>
                  <ChatListItem
                    avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                    talkDate="2020/08/08 22:15:30"
                    talkContent="咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？咋不回复我呢？"></ChatListItem>
                  <ChatListItem
                    avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                    talkDate="2020/08/08 22:19:30"
                    talkContent="在的呢！"
                    talkSide></ChatListItem>
                  <ChatListItem
                    avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                    talkDate="2020/08/08 2020/08/08 22:19:31"
                    talkContent="在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？在的呢！你在做什么呢？"
                    talkSide></ChatListItem>
                </List>
              </Scroll>
            </Content>
            <div>
              <Input style="width: 100%;" type="textarea" :rows="4" placeholder="请输入聊天消息"/>
            </div>
            <div style="background-color: white;">
              <Button style="background-color: rgba(70,76,91,.9);color: white;float: right;margin-top: 10px;margin-right: 10px;margin-bottom: 10px;" @click="login">登录</Button>
              <Button style="background-color: rgba(70,76,91,.9);color: white;float: right;margin-top: 10px;margin-right: 10px;margin-bottom: 10px;">发送</Button>
            </div>
          </Layout>
        </TabPane>
        <TabPane label="啊勋" icon="logo-windows">标签二的内容</TabPane>
        <TabPane label="9级宝石" icon="logo-tux">标签三的内容</TabPane>
      </Tabs>
    </Layout>
  </div>
</template>

<script>
import ChatListItem from '@/components/chat/chatList/ChatListItem'
export default {
  name: 'chatMain',
  components: {ChatListItem},
  data () {
    return {
      friendData: [
        {
          title: '我的好友',
          expand: false,
          children: [
            {
              title: '9级宝石'
            },
            {
              title: '大斌'
            }
          ]
        },
        {
          title: '同学',
          expand: false,
          children: [
            {
              title: '老三'
            },
            {
              title: '啊勋'
            },
            {
              title: '大伟'
            },
            {
              title: '决战man'
            },
            {
              title: '决战boy'
            },
            {
              title: '无敌小伙子'
            }
          ]
        }
      ],
      wsuri: 'ws://127.0.0.1:8399/chat'
    }
  },
  created () {
    this.initWebSocket()
  },
  methods: {
    login () {
      let actions = {'url': '/chat/login/', 'params': {'token': this.$store.getters.token}}
      this.websocketsend(JSON.stringify(actions))
    },
    initWebSocket () { // 初始化weosocket
      this.websock = new WebSocket(this.wsuri)
      this.websock.onmessage = this.websocketonmessage
      this.websock.onopen = this.websocketonopen
      this.websock.onerror = this.websocketonerror
      this.websock.onclose = this.websocketclose
    },
    websocketonopen () { // 连接建立之后执行send方法发送数据
    },
    websocketonerror () { // 连接建立失败重连
      console.log('--出错了-')
      this.initWebSocket()
    },
    websocketonmessage (e) { // 数据接收
      const redata = JSON.parse(e.data)
      console.log('后端发送过来的信息是：' + redata)
    },
    websocketclose (e) { // 关闭
      console.log('断开连接', e)
    },
    websocketsend (Data) { // 数据发送
      if (this.websock.readyState === 1) {
        this.websock.send(Data)
      } else {
        setTimeout(() => {
          this.websock = new WebSocket(this.wsuri)
          this.websock.onmessage = this.websocketonmessage
          this.websock.onopen = this.websocketonopen
          this.websock.onerror = this.websocketonerror
          this.websock.onclose = this.websocketclose
          setTimeout(() => { this.websocketsend(Data) }, 1000)
        }, 1000)
      }
    }
  }
}
</script>

<style scoped>
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    height: 100%;
  }
</style>
