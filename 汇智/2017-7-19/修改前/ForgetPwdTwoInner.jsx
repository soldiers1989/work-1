import { List, InputItem, WingBlank, Button } from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import '../RegisterStepOne/register.less';

// 忘记密码（步骤二）页内部组件
class ForgetPwdTwoInner extends React.Component {
  render() {
    const { getFieldProps } = this.props.form;
    return (
      <div>
          <List className="register-as-list">
            <InputItem className="register-list-item"
              {...getFieldProps('password')}
              type="password"
              placeholder="设置密码"
            />
          </List>
          <List className="register-as-list">
            <div>
              <InputItem className="register-list-item"
                {...getFieldProps('password')}
                type="password"
                placeholder="确认密码"
              />
            </div>
          </List>
        <WingBlank>
          <div className="btn-container">
            <Link to="index/Index">
              <Button
                className="btn-next-step" type="primary" onClick={() => {
                }}
              >确定</Button>
            </Link>
          </div>
        </WingBlank>
      </div>
    );
  }
}

const ForgetPwdTwoInnerWrapper = createForm()(ForgetPwdTwoInner);
export default ForgetPwdTwoInnerWrapper;
