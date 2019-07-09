<template>
  <div>
    <div class="demo-upload-list" v-for="item in uploadList">
      <template v-if="item.status === 'finished'">
        <img :src="item.url">
        <div class="demo-upload-list-cover">
          <Icon type="ios-eye-outline" @click.native="handleView(item.name)"></Icon>
          <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
        </div>
      </template>
      <template v-else>
        <Progress v-if="item.showProgress" :percent="item.percentage" hide-info></Progress>
      </template>
    </div>
    <Upload
      ref="upload"
      :show-upload-list="false"
      :default-file-list="defaultList"
      :on-success="uploadSuccess"
      :format="['jpg','jpeg','png']"
      :max-size="2048"
      :on-format-error="handleFormatError"
      :on-exceeded-size="handleMaxSize"
      :before-upload="handleBeforeUpload"
      :multiple="multiple"
      type="drag"
      :action="uploadAction"
      :data="uploadData"
      :headers="uploadHeader"
      style="display: inline-block;width:58px;">
      <div style="width: 58px;height:58px;line-height: 58px;">
        <Icon type="ios-camera" size="20"></Icon>
      </div>
    </Upload>
    <Modal  v-model="visible" :fullscreen="fullscreen" :footer-hide="footerHide">
      <img :src="imgName" v-if="visible" style="width: 100%">
    </Modal>
  </div>
</template>
<script>
  import userStore from '../../store/module/user';

  export default {
    name: 'baseImgUpload',
    props: {
      idImageType: {
        type: String,
        default: 'tmp'
      },
      maxLength: {
        type: Number,
        default: 1
      },
      multiple: {
        type: Boolean,
        default: false
      },
      defaultList: {
        type: Array,
        default: () => {
          return [];
        }
      }
    },
    data() {
      return {
        imgName: '',
        visible: false,
        fullscreen: true,
        footerHide: true,
        uploadList: [],
        uploadAction: this.$runConfig.runConfig.baseUrl + '/file/uploadFile',
        imgUrl: this.$runConfig.runConfig.imgUrl + '/',
        uploadData: {},
        uploadHeader: {'x-access-token': userStore.state.token}
      };
    },
    methods: {
      cleanAll() {// 父组件调用该方法来清空该组件的uploadList的集合
        this.uploadList = [];
      },
      handleView(name) {
        this.imgName = this.imgUrl + name;
        this.visible = true;
      },
      handleRemove(file) {
        this.uploadList.splice(this.uploadList.indexOf(file), 1);
      },
      uploadSuccess(res, file) {
        if (res.code == 200) {
          file.url = this.$runConfig.runConfig.imgUrl + '/' + res.obj.url;
          file.name = res.obj.name;
          this.uploadList.push(file);
          console.log('this.uploadList=>'+JSON.stringify(this.uploadList))
        } else {
          this.$Notice.warning({
            title: '文件上传失败',
            desc: '失败原因： ' + res.msg
          });
        }

      },
      handleMaxSize(file) {
        this.$Notice.warning({
          title: '超过文件大小限制',
          desc: '文件  ' + file.name + ' 超过2M的大小.'
        });
      },
      handleBeforeUpload() {
        const check = this.uploadList.length < this.maxLength;
        if (!check) {
          this.$Notice.warning({
            title: '最多只能上传' + this.maxLength + '个文件.'
          });
        }
        return check;
      },
      handleFormatError(file) {
        this.$Notice.warning({
          title: '文件格式不正确',
          desc: file.name + '的文件格式不对,请选择文件为jpg或者png的文件进行上传.'
        });
      },
    },
    mounted() {
      this.uploadList = this.$refs.upload.fileList;
      this.uploadData = {
        idImageType: this.idImageType
      };
    },
    watch: {
      uploadList(val) {// 更新父组件的图片的集合数据
        this.$emit('getUploadList', val);
      },
      defaultList(val) {
        this.uploadList = val;
      }
    }
  };
</script>
<style>
  .demo-upload-list {
    display: inline-block;
    width: 60px;
    height: 60px;
    text-align: center;
    line-height: 60px;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
    margin-right: 4px;
  }

  .demo-upload-list img {
    width: 100%;
    height: 100%;
  }

  .demo-upload-list-cover {
    display: none;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, .6);
  }

  .demo-upload-list:hover .demo-upload-list-cover {
    display: block;
  }

  .demo-upload-list-cover i {
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
  }
</style>
