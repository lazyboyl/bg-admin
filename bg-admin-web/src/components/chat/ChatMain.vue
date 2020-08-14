<template>
  <div class="layout">
    <Layout>
      <Sider hide-trigger :style="{background: '#fff'}" width="260">
        <Tabs style="min-height: 494px;">
          <TabPane label="消息" icon="logo-apple">
            <List size="small">
              <ListItem>
                <ListItemMeta avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                              title="spring爱好交流群" description="技术大神在吗？"/>
                <template slot="extra">
                  <div style="display: inline-block;">10:20</div>
                  <Badge :count="3" type="normal">
                  </Badge>
                </template>
              </ListItem>
              <ListItem>
                <ListItemMeta avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                              title="决战MAN" description="无聊死了"/>
                <template slot="extra">
                  <div style="display: inline-block;">16:20</div>
                  <Badge :count="3" type="normal">
                  </Badge>
                </template>
              </ListItem>
              <ListItem>
                <ListItemMeta avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar"
                              title="大伟哥" description="快来玩LOL"/>
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
              <Poptip :transfer=true style="float: right;" placement="bottom-end">
                <Button type="text" size="small" icon="md-add"></Button>
                <ul class="ivu-dropdown-menu" slot="content">
                  <li class="ivu-dropdown-item" @click="addFriendShow">添加好友</li>
                  <li class="ivu-dropdown-item ivu-dropdown-item-divided" @click="addGroupShow">创建群组</li>
                </ul>
              </Poptip>
            </div>
            <div style="margin-top: 5px;margin-left: 5px;">
              <Tree :data="friendData" @on-select-change="talkShow" @on-contextmenu="handleContextMenu">
                <template slot="contextMenu">
                  <DropdownItem @click.native="addUserGroupShow">新增分组</DropdownItem>
                  <DropdownItem @click.native="deleteUserGroup" style="color: #ed4014">删除分组</DropdownItem>
                </template>
              </Tree>
            </div>
          </TabPane>
        </Tabs>
      </Sider>
      <Tabs style="width:100%;" closable type="card" v-model="nowChatUser" @on-tab-remove="removeChatUser">
        <TabPane :label="tp.title" :name="tp.userId" v-for="tp in chatTabPane" :key="tp.userId" v-if="tp.isShow">
          <Layout>
            <Content style="margin-left: 20px;">
              <Scroll>
                <List>
                  <li style="text-align: center;">
                    <a href="javascript:void(0)">查看更多消息</a>
                  </li>
                  <ChatListItem v-for="cli in tp.chatListItems" :key="cli.receiveDate" :talkDate="cli.receiveDate"
                                :talkContent="cli.messageContent" :talkSide="cli.talkSide"
                                avatar="https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar">
                  </ChatListItem>
                </List>
                <span :id="'msg_end_' + tp.userId" style="overflow:hidden"></span>
              </Scroll>
            </Content>
            <div>
              <Input style="width: 100%;" type="textarea" v-model="tp.messageContent" :rows="4" placeholder="请输入聊天消息"/>
            </div>
            <div style="background-color: white;">
              <Button
                style="background-color: rgba(70,76,91,.9);color: white;float: right;margin-top: 10px;margin-right: 10px;margin-bottom: 10px;"
                @click="login">登录
              </Button>
              <Button
                style="background-color: rgba(70,76,91,.9);color: white;float: right;margin-top: 10px;margin-right: 10px;margin-bottom: 10px;"
                @click="sendMsg(tp)">
                发送
              </Button>
            </div>
          </Layout>
        </TabPane>
      </Tabs>
    </Layout>
    <addFriendGroup v-model="addFriendGroupShow" v-on:operateFriendData="operateFriendData"></addFriendGroup>
  </div>
</template>

<script>
    import ChatListItem from '@/components/chat/chatList/ChatListItem'
    import addFriendGroup from "@/components/chat/friendGroup/addFriendGroup";
    import {myFriendList, deleteFriendGroup} from '@/api/chat/friend/friend.api';

    export default {
        name: 'chatMain',
        components: {
            ChatListItem,
            addFriendGroup
        },
        data() {
            return {
                addFriendGroupShow: false,
                nowChatUser: '',
                chatTabPane: [],
                friendData: [],
                rightClickData: {},
                wsuri: 'ws://127.0.0.1:8399/chat'
            }
        },
        created() {
            this.initWebSocket()
            this.initFriendGroup()
        },
        methods: {
            operateFriendData(data) {
                console.log(data)
                this.friendData.push({
                    title: data.friendGroupName,
                    expand: false,
                    contextmenu: true,
                    allowDeletion: data.allowDeletion,
                    friendGroupId: data.friendGroupId,
                    children: []
                })
            },
            deleteUserGroup() {
                if (this.rightClickData.allowDeletion === '9') {
                    this.$Message.warning('根好友分组不允许删除！');
                    return false
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>是否删除【' + this.rightClickData.title + '】好友分组</p>',
                    onOk: () => {
                        deleteFriendGroup({friendGroupId: this.rightClickData.friendGroupId}).then(res => {
                            if (res.code == 200) {
                                this.$Message.success(res.msg);
                                for (let i = 0; i < this.friendData.length; i++) {
                                    if (this.friendData[i].friendGroupId === this.rightClickData.friendGroupId) {
                                        for (let j = 0; j < this.friendData[i].children.length; j++) {
                                            this.friendData[0].children.push(this.friendData[i].children[j])
                                        }
                                        this.friendData.splice(i, 1)
                                        break
                                    }
                                }
                            } else {
                                this.$Message.error(res.msg);
                            }
                        })
                    },
                    onCancel: () => {
                        this.$Message.info('取消了删除');
                    }
                })
            },
            addUserGroupShow() {
                this.addFriendGroupShow = true
            },
            handleContextMenu(data) {
                this.rightClickData = data;
            },
            addGroupShow() {
                console.log('addGroupShow')
            },
            addFriendShow() {
                console.log('addFriendShow')
            },
            sendMsg(tp) {
                let actions = {
                    'url': '/chat/sendMessage/',
                    'params': {
                        'token': this.$store.getters.token,
                        'messageContent': tp.messageContent,
                        'receiveUserId': tp.userId
                    }
                }
                this.websocketsend(JSON.stringify(actions))
                tp.chatListItems.push({
                    sendUserId: tp.belowUserId,
                    receiveUserId: tp.userId,
                    receiveDate: this.formatDate(new Date(), 'yyyy/MM/dd hh:mm:ss'),
                    messageContent: tp.messageContent,
                    talkSide: true
                })
                tp.messageContent = ''
                this.scrollTalkContent(tp.userId)
            },
            removeChatUser(userId) {
                for (let i = 0; i < this.chatTabPane.length; i++) {
                    if (this.chatTabPane[i].userId === userId) {
                        this.chatTabPane.splice(i, 1)
                    }
                }
            },
            talkShow(data) {
                if (data.length > 0) {
                    if (data[0].userId === undefined) {
                        console.log('好友节点')
                        return false;
                    }
                    for (let i = 0; i < this.chatTabPane.length; i++) {
                        if (this.chatTabPane[i].userId === data[0].userId) {
                            this.nowChatUser = data[0].userId
                            return false
                        }
                    }
                    this.chatTabPane.push({
                        userId: data[0].userId,
                        belowUserId: data[0].belowUserId,
                        title: data[0].title,
                        isShow: true,
                        page: 0,
                        pageSize: 10,
                        messageContent: '',
                        chatListItems: []
                    })
                    this.nowChatUser = data[0].userId
                }
            },
            initFriendGroup() {
                myFriendList({}).then(res => {
                    if (res.code == 200) {
                        let myFriendData = [];
                        for (let i = 0; i < res.obj.length; i++) {
                            let obj = {
                                title: res.obj[i].friendGroupName,
                                expand: false,
                                contextmenu: true,
                                allowDeletion: res.obj[i].allowDeletion,
                                friendGroupId: res.obj[i].friendGroupId,
                                children: []
                            }
                            if (res.obj[i].friendLists != undefined && res.obj[i].friendLists.length > 0) {
                                for (let j = 0; j < res.obj[i].friendLists.length; j++) {
                                    if(res.obj[i].friendLists[j].nickName !== null){
                                        obj.children.push({
                                            title: res.obj[i].friendLists[j].nickName,
                                            userId: res.obj[i].friendLists[j].userId,
                                            friendState: res.obj[i].friendLists[j].friendState,
                                            belowUserId: res.obj[i].friendLists[j].belowUserId
                                        });
                                    }
                                }
                            }
                            myFriendData.push(obj);
                        }
                        this.friendData = myFriendData;
                    } else {
                        this.$Message.error(res.msg);
                    }
                })
            },
            login() {
                let actions = {'url': '/chat/login/', 'params': {'token': this.$store.getters.token}}
                this.websocketsend(JSON.stringify(actions))
            },
            initWebSocket() { // 初始化weosocket
                this.websock = new WebSocket(this.wsuri)
                this.websock.onmessage = this.websocketonmessage
                this.websock.onopen = this.websocketonopen
                this.websock.onerror = this.websocketonerror
                this.websock.onclose = this.websocketclose
            },
            websocketonopen() { // 连接建立之后执行send方法发送数据
            },
            websocketonerror() { // 连接建立失败重连
                console.log('--出错了-')
                this.initWebSocket()
            },
            websocketonmessage(e) { // 数据接收
                const returnDate = JSON.parse(e.data)
                if (returnDate.msgType === '1') {
                    let data = returnDate.data
                    for (let i = 0; i < this.chatTabPane.length; i++) {
                        if (this.chatTabPane[i].userId === data.sendUserId) {
                            this.chatTabPane[i].chatListItems.push({
                                sendUserId: data.sendUserId,
                                receiveUserId: data.receiveUserId,
                                receiveDate: this.formatDate(new Date(data.receiveDate), 'yyyy/MM/dd hh:mm:ss'),
                                messageContent: data.messageContent,
                                talkSide: false
                            })
                            this.scrollTalkContent(data.sendUserId)
                        }
                    }

                }
                console.log('后端发送过来的信息是：' + returnDate)
            },
            websocketclose(e) { // 关闭
                console.log('断开连接', e)
            },
            websocketsend(Data) { // 数据发送
                if (this.websock.readyState === 1) {
                    this.websock.send(Data)
                } else {
                    setTimeout(() => {
                        this.websock = new WebSocket(this.wsuri)
                        this.websock.onmessage = this.websocketonmessage
                        this.websock.onopen = this.websocketonopen
                        this.websock.onerror = this.websocketonerror
                        this.websock.onclose = this.websocketclose
                        setTimeout(() => {
                            this.websocketsend(Data)
                        }, 1000)
                    }, 1000)
                }
            },
            scrollTalkContent(userId) {
                this.$nextTick(() => {
                    setTimeout(() => {
                        document.getElementById('msg_end_' + userId).scrollIntoView(false)
                    }, 13)
                })
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
