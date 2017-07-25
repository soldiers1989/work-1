import { List, InputItem, WingBlank, Button, Modal} from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import './register.less'
import crypto from  'crypto';
import config from '../../../config';
import request from '../../../utils/requestPOST';
import axios from 'axios';
import Qs from 'qs';

const alert = Modal.alert;
const codeImg = "http://222.73.203.71:8080/WiseAuth/AuthImageServlet";
// 注册页内部组件
class RegisterStepOneInner extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      imgUrl: config.codeImgUrl
    };
  }
  //每次进入这个页面修改图片验证码
  componentWillMount () {
    var nowDate = new Date().getTime();
    var ImgUrl = config.codeImgUrl + "?" +nowDate;
    this.setState({
      imgUrl : ImgUrl
    })
  }
  //点击更换验证码
  changeImg = () =>{
    var nowDate = new Date().getTime();
    console.log(nowDate);
    var ImgUrl = codeImg + "?" +nowDate;
    this.setState({
      imgUrl : ImgUrl
    })
  }
  //验证用户名的格式是否正确
  validateAccount = (rule, value, callback) => {
    var reg = /^[a-zA-Z][a-zA-Z0-9]{7,19}$/i;
    var r = reg.test(value);
    if(r==false){
      callback(new Error('用户名以字母开头，长度为8-20位，且只能含有字母和数字'));
    }{
      callback();
    }
  }
  /*验证密码*/
  validatePassword = (rule, value, callback) => {
    var rn = /\d+/g; //数字测试
    var ra = /[a-zA-Z]/g; //字母测试
    if (value && value.length >= 8) {
      if(rn.test(value) && ra.test(value)) {
        callback();
      }else{
        callback(new Error('密码需包含数字和字母'));
      }
    }else{
      callback(new Error('新密码至少8个字符'));
    }
  }
  /*验证手机号*/
  validatePhoneNo = (rule, value, callback) => {
    this.focus;
    value = value.replace(" ","").replace(" ","");
    var re = /^1[34578]\d{9}$/;
    if(re.test(value)){
      callback();
    }else{
      callback(new Error('手机号格式错误！'));
    }
  }
  //获取手机验证码
  getMessageCode = () =>{
    this.props.form.validateFields({ force: true }, (error) => {
      if(!error){
        var registerInfo = this.props.form.getFieldsValue();
        var code = registerInfo.code;
        var phone = registerInfo.phoneNo;
        phone=phone.replace(" ","").replace(" ","");
        var url = config.getPhoneCode + phone + '/' + code;
        // console.log(code+phone);
        console.log(url);
        axios.get(url).then(function(response){//从配置文件中读取url，GET请求
          console.log("Register response",response);
          if(response.data.success){
            alert("验证码发送成功！");
          }else{
            alert(response.data.msg);
          }
        });
      }
    });
  }
  //表单提交
  onSubmit = () => {
    this.props.form.validateFields({ force: true }, (error) => {
      if (!error) {
        var registerInfo = this.props.form.getFieldsValue();
        console.log(registerInfo);
        var username = registerInfo.username;
        var password = crypto.createHash('md5').update(registerInfo.password,'').digest('hex');
        var repassword = crypto.createHash('md5').update(registerInfo.repassword,'').digest('hex');
        if(password != repassword){
          alert("两次输入的密码不相同！");
          return;
        }
        var phone = registerInfo.phoneNo;
        phone=phone.replace(" ","").replace(" ","");
        var code = registerInfo.messageCode;
        var params = "username="+username+"&password="+password+"&phone="+phone+"&code="+code;
        var data = {
          username: username,
          password: password,
          phone:phone,
          code:code
        };
        console.log(params);
        axios.post(config.registerUrl,Qs.stringify(data)).then(function(response){//从配置文件中读取url，GET请求
          // console.log("registerUrl response",response);
          if(response.data.success){
            alert("注册成功！");
            //用户登录信息（用户名，MD5加密后的密码）存入缓存
            var login = {"username":username,"pwd":password};
            var loginInfo = JSON.stringify(login);
            localStorage.loginInfo = loginInfo;
            var dataLogin = {
              j_password: password,
              j_username: username,
              lgtp: "front"
            };
            //自动登录
            axios.post(config.loginUrl,Qs.stringify(dataLogin)).then(function(response){//从配置文件中读取url，POST请求
              var reData = response.data;
              if(reData.success){//登陆成功
                //获取用户的个人信息并存入缓存
                axios.get(config.userInfoUrl).then(function(response){//从配置文件中读取url，GET请求
                  var  userInfo = response.data;
                  var login = {"username":username,"pwd":""};//注册的时候只存用户名进缓存
                  var loginInfo = JSON.stringify(login);
                  userInfo = JSON.stringify(userInfo);

                  localStorage.userInfo = userInfo;//个人信息存入缓存
                  localStorage.loginInfo = loginInfo;//用户登录信息（用户名，MD5加密后的密码）存入缓存

                  window.location.href="#index/Index";
                });
              }else{//登录验证失败
                alert(reData.msg);
                this.props.form.resetFields();
              }
            }).catch(function(error){
              console.log(error);
            });
          }else{
            alert(response.data.msg);
          }
        });
      } else {
        alert('校验失败');
      }
    });
  }
  render() {
    const {imgUrl} = this.state;
    const { getFieldProps, getFieldError } = this.props.form;
    return (
      <form>
        <List className="register-as-list">
          {/*用户名输入栏，并添加验证*/}
          <InputItem className="register-list-item"
            {...getFieldProps('username',{
                      rules:[
                        { required: true, message: '请填写用户名' },
                        { validator: this.validateAccount },
                      ]
                    })}
                    clear
                    error={!!getFieldError('username')}
                    onErrorClick={() => {
                      alert(getFieldError('username').join('、'));
                    }}
                     maxLength = "20"
            placeholder="用户名（字母开头8-20位的字母数字）"
          />
        </List>
        <List className="register-as-list">
          <InputItem className="register-list-item"
                     {...getFieldProps('password',{
                       rules:[
                         { required: true, message: '请填写密码' },
                         { validator: this.validatePassword },
                       ]
                     })}
                     clear
                     error={!!getFieldError('password')}
                     onErrorClick={() => {
                       alert(getFieldError('password').join('、'));
                     }}
            type="password"
            placeholder="密码（八位以上字母和数字）"
          />
        </List>
        <List className="register-as-list">
          <InputItem className="register-list-item"
                     {...getFieldProps('repassword',{
                       rules:[
                         { required: true, message: '请确认密码' },
                         { validator: this.validatePassword },
                       ]
                     })}
                     clear
                     error={!!getFieldError('repassword')}
                     onErrorClick={() => {
                       alert(getFieldError('repassword').join('、'));
                     }}
            type="password"
            placeholder="确认密码"
          />
        </List>
          <List className="register-as-list">
            <InputItem className="register-list-item"
              type="phone"
                {...getFieldProps('phoneNo',{
                  rules:[
                    { required: true, message: '请输入手机号' },
                    { validator: this.validatePhoneNo },
                  ]
                })}
                       clear
                       error={!!getFieldError('phoneNo')}
                       onErrorClick={() => {
                         alert(getFieldError('phoneNo').join('、'));
                       }}
              placeholder="手机号"
            />
          </List>
        <div className="register-verify-code-right">
          <Link>
            <img
              src={imgUrl} className="register-verify-code-image"
             alt="image"
              onClick={this.changeImg}
            />
          </Link>
        </div>
        <List className="register-input-code">
          <InputItem className="register-list-item"
            {...getFieldProps('code')}
            placeholder="验证码"
          />
        </List>
          <Button
            className="register-btn-getCode" type="primary" inline size="small" onClick={this.getMessageCode}
          >获取验证码</Button>
          <List className="register-input-code">
            <InputItem className="register-list-item"
              {...getFieldProps('messageCode')}
              placeholder="短信验证码"
            />
          </List>
        <WingBlank>
          <div className="btn-container">
              <Button
                className="btn-next-step" type="primary" onClick={this.onSubmit}
              >注册</Button>
          </div>
        </WingBlank>
      </form>
    );
  }
}

const RegisterStepOneInnerWrapper = createForm()(RegisterStepOneInner);
export default RegisterStepOneInnerWrapper;
