<template>
  <li  class="ivu-list-item" >
    <div  class="ivu-list-item-meta">
      <div class="ivu-list-item-meta-avatar" v-if="!talkSide">
        <span class="ivu-avatar ivu-avatar-circle ivu-avatar-image ivu-avatar-default">
          <img :src="avatar">
        </span>
      </div>
      <div class="ivu-list-item-meta-content" >
        <div class="ivu-list-item-meta-title" style="text-align: center;">{{talkDate}}</div>
        <div class="ivu-list-item-meta-description" style="text-align: justify;" v-if="!talkSide">
          <div class="ivu-tooltip-dark"  x-placement="right-end">
            <div class="ivu-tooltip-content">
              <div class="ivu-tooltip-arrow"></div>
              <div class="ivu-tooltip-inner ivu-tooltip-inner-with-width" >{{talkContent}}</div>
            </div>
          </div>
        </div>
        <div class="ivu-list-item-meta-description" style="text-align: right;"  v-if="talkSide">
          <div style="text-align: left;display: inline-block;">
            <div class="ivu-tooltip-dark"  x-placement="right-end" style="background-color: teal;">
              <div class="ivu-tooltip-content">
                <div class="ivu-tooltip-arrow"></div>
                <div class="ivu-tooltip-inner ivu-tooltip-inner-with-width" >{{talkContent}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="ivu-list-item-meta-avatar" v-if="talkSide">
        <span class="ivu-avatar ivu-avatar-circle ivu-avatar-image ivu-avatar-default">
          <img :src="avatar">
        </span>
      </div>
    </div> <!---->
    <div class="ivu-list-item-extra"></div>
  </li>
</template>
<script>
export default {
  name: 'ChatListItem',
  props: {
    talkDate: {
      type: String,
      default: ''
    },
    avatar: {
      type: String,
      default: ''
    },
    talkContent: {
      type: String,
      default: ''
    },
    talkSide: {
      type: Boolean,
      default: false
    }
  }
}
</script>
