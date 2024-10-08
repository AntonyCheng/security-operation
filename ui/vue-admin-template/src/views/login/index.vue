<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >

      <div class="title-container">
        <h3 class="title">安全运维管理平台</h3>
      </div>

      <el-form-item prop="account">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="account"
          v-model="loginForm.account"
          placeholder="请输入用户账号"
          name="account"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="请输入用户密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item prop="captcha.code" class="captcha-code-form">
        <span class="svg-container">
          <svg-icon icon-class="example" />
        </span>
        <el-input
          ref="captcha.code"
          v-model="loginForm.captcha.code"
          placeholder="请输入验证码结果"
          name="captcha.code"
          type="text"
          tabindex="3"
          auto-complete="on"
        >
          <template slot="append">
            <span class="code-container">
              <img alt="captcha.imgBase64" :src="imgBase64" onerror="" @click="getCaptchaBase64">
            </span>
          </template>
        </el-input>
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleLogin"
      >登录
      </el-button>

    </el-form>
  </div>
</template>

<script>
import { captcha } from '@/api/captcha'
import { getRsaKey } from '@/api/encrypt'
import { JSEncrypt } from 'jsencrypt'

export default {
  name: 'Login',
  data() {
    const validateAccount = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('账号不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('密码不能为空'))
      } else {
        callback()
      }
    }
    const validateCode = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('验证码不能为空'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        account: '',
        password: '',
        captcha: {
          code: '',
          uuid: ''
        }
      },
      loginRules: {
        'account': [{ required: true, trigger: 'blur', validator: validateAccount }],
        'password': [{ required: true, trigger: 'blur', validator: validatePassword }],
        'captcha.code': [{ required: true, trigger: 'blur', validator: validateCode }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      imgBase64: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCaptchaBase64()
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          this.loading = true
          const encryptor = new JSEncrypt()
          const encryptKey = await this.getEncryptKey()
          encryptor.setPublicKey(encryptKey)
          this.loginForm.password = encryptor.encrypt(this.loginForm.password)
          this.$store.dispatch('auth/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loginForm.password = ''
            this.loading = false
          }).catch(() => {
            this.loginForm.password = ''
            this.loading = false
            this.getCaptchaBase64()
          })
        } else {
          console.log('用户账号密码校验失败')
          return false
        }
      })
    },
    getCaptchaBase64() {
      captcha().then(response => {
        this.loginForm.captcha.uuid = response.data.uuid
        this.imgBase64 = 'data:image/gif;base64,' + response.data.imgBase64
      })
    },
    async getEncryptKey() {
      const response = await getRsaKey()
      return response.msg
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .captcha-code-form {

    .el-input {
      width: 90%;
      display: inline-table;

    }

    .code-container {
      padding-top: 5px;
      display: inline-block;
      img {
        height: 40px;
        display: inline-block;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-weight: bolder;
      font-size: 35px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
