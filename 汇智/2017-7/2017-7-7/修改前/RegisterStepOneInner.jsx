import { List, InputItem, WingBlank, Button } from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import './register.less'

// 注册页内部组件
class RegisterStepOneInner extends React.Component {
  //验证用户名的格式是否正确
  validateAccount = (rule, value, callback) => {
    if (value && value.length >= 8) {
      callback();
    } else {
      callback(new Error('用户名至少8个字符'));
    }
  }
  render() {
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
            placeholder="请填写用户名"
          />
        </List>
        <List className="register-as-list">
          <InputItem className="register-list-item"
            {...getFieldProps('password')}
            type="password"
            placeholder="请填写密码"
          />
        </List>
        <List className="register-as-list">
          <InputItem className="register-list-item"
            {...getFieldProps('password')}
            type="password"
            placeholder="请确认密码"
          />
        </List>
          <List className="register-as-list">
            <InputItem className="register-list-item"
              type="phone"
              {...getFieldProps('phoneNo')}
              placeholder="请输入手机号"
            />
          </List>
        <div className="register-verify-code-right">
          <Link>
            <img
              src="http://222.73.203.71:8080/WiseAuth/AuthImageServlet" className="register-verify-code-image"
              style={{ }} alt="image"
              onClick={() => {}}
            />

          </Link>
        </div>
        <List className="register-input-code">
          <InputItem className="register-list-item"
            {...getFieldProps('code')}
            placeholder="请输入验证码"
          />
        </List>
          <Button
            className="register-btn-getCode" type="primary" inline size="small" onClick={() => {
            }}
          >获取验证码</Button>
          <List className="register-input-code">
            <InputItem className="register-list-item"
              {...getFieldProps('messageCode')}
              placeholder="请输入短信验证码"
            />
          </List>
        <WingBlank>
          <div className="btn-container">
            <Link to="index/Index">
              <Button
                className="btn-next-step" type="primary" onClick={() => {
                }}
              >注册</Button>
            </Link>
          </div>
        </WingBlank>
      </form>
    );
  }
}

const RegisterStepOneInnerWrapper = createForm()(RegisterStepOneInner);
export default RegisterStepOneInnerWrapper;
