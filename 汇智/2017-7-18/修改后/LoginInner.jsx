import { Modal,List, InputItem, WingBlank, Button, Checkbox, Flex} from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import axios from 'axios';
import Qs from 'qs';

import './Login.less';
import crypto from  'crypto';
import config from '../../../config';

const alert = Modal.alert;
const CheckboxItem = Checkbox.CheckboxItem;
const AgreeItem = Checkbox.AgreeItem;
// 登录页内部组件
class LoginInner extends React.Component {
  constructor (props) {
    super(props);
  }
  //记住密码的默认状态，false
  state = {
    rememberPwd: false
  }
  //记住密码
  rememberPwd(e) {
    if (e.target.checked) {
      this.state.rememberPwd = true;
      console.log("记住密码" + this.state.rememberPwd);
    } else {
      this.state.rememberPwd = false;
      console.log("记住密码" + this.state.rememberPwd);
    }
  }
  onSubmit = () => {
    this.props.form.validateFields({ force: true }, (error) => {
      if (!error) {
        var logInfo = this.props.form.getFieldsValue();
        var userName = logInfo.username;
        var pwd = logInfo.password;
        var rememberPwd = this.state.rememberPwd;
        //MD5加密
        var sign = crypto.createHash('md5').update(pwd,'').digest('hex');
        var data = {
          j_password: sign,
          j_username: userName,
          lgtp: "front"
        };
        //post请求
           axios.post(config.loginUrl,Qs.stringify(data)).then(function(response){//从配置文件中读取url，POST请求
             var reData = response.data;
             if(reData.success){//登陆成功
               //获取用户的个人信息并存入缓存
               axios.get(config.userInfoUrl).then(function(response){//从配置文件中读取url，GET请求
                 var  userInfo = response.data;
                 userInfo = JSON.stringify(userInfo);
                 var login = {"username":userName};
                 //判断是否记住密码
                 if(rememberPwd){
                   login = {"username":userName,"pwd":sign};
                 }
                 var loginInfo = JSON.stringify(login);

                 localStorage.userInfo = userInfo;//个人信息存入缓存
                 localStorage.loginInfo = loginInfo;//用户登录信息（用户名，MD5加密后的密码）存入缓存
                 if(localStorage.lastUrl != undefined && localStorage.lastUrl !="undefined"){
                   var lastUrl = localStorage.lastUrl;
                   if (lastUrl== "index/Bound" && userInfo.cardid==null ) {
                     lastUrl="index/unbound";
                   }
                   //跳转到上一个状态
                   window.location.href="#"+lastUrl;
                 }else{
                   //跳转首页
                   window.location.href="#index/Index";
                 }
               });
             }else{//登录验证失败
               alert(reData.msg);
               this.props.form.resetFields();
             }
            }).catch(function(error){
              console.log(error);
           });
      } else {
        alert('校验失败');
      }
    });
  }
  validateAccount = (rule, value, callback) => {
    if (value && value.length > 0) {
      callback();
    } else {
      callback(new Error('帐号至少4个字符'));
    }
  }

  render() {
    const {getFieldProps, getFieldError} = this.props.form;
    return (
      <form>
        <div className="login-wrap">
              <List className="listTop">
                <InputItem className="login-list-item"
                  {...getFieldProps('username',{
                    // initialValue: '小蚂蚁',
                    rules: [
                  { required: true, message: '请输入帐号' },
                  { validator: this.validateAccount },
                    ],
                  })}
                  clear
                  error={!!getFieldError('username')}
                  onErrorClick={() => {
                    alert(getFieldError('username').join('、'));
                  }}
                  placeholder="请输入您的手机号/用户名"
                />
              </List>
              <List className="listTop">
                <InputItem className="login-list-item"
                  {...getFieldProps('password',{
                    // initialValue: '小蚂蚁',
                    rules: [
                      { required: true, message: '请输入密码' },
                      { validator: this.validateAccount },
                    ],
                  })}
                  clear
                  error={!!getFieldError('password')}
                  onErrorClick={() => {
                    alert(getFieldError('password').join('、'));
                  }}
                  type="password"
                  placeholder="请输入6-20位密码"
                />
              </List>
              <List className="listTop login-am-list-body">
                <AgreeItem className="login-rem-pwd"
                           data-seed="logId"
                           onChange={(e) => this.rememberPwd(e)}>
                  <span className="login-rem-pwd_font">记住密码</span>
                </AgreeItem>
              </List>
              <WingBlank>
                <div>
                    <Button
                      className="btn-login" type="primary" onClick={this.onSubmit} inline
                    >登录</Button>
                </div>

                <div className="login-pwd-text">
                  <a href="#ForgetPwdOne">忘记密码</a>
                  <a className="login-pwd-text-register" href="#RegisterStepOne">立即注册</a>
                </div>
              </WingBlank>
        </div>
      </form>
    );
  }
}

const LoginInnerWrapper = createForm()(LoginInner);
export default LoginInnerWrapper;
