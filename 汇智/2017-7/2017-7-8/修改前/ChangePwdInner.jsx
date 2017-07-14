import { List, InputItem, WingBlank, Button } from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import '../RegisterStepOne/register.less';


// 修改密码页内部组件
class ChangePwdInner extends React.Component {
  render() {
    const { getFieldProps } = this.props.form;
    return (
      <div>
          <List className="register-as-list">
            <InputItem className="register-list-item"
              {...getFieldProps('oldPassword')}
              type="password"
              placeholder="请输入原密码"
            />
          </List>
          <List className="register-as-list">
            <InputItem className="register-list-item"
              {...getFieldProps('newPassword')}
              type="password"
              placeholder="请输入修改密码"
            />
          </List>
          <List className="register-as-list">
            <div>
              <InputItem className="register-list-item"
                {...getFieldProps('newPassword')}
                type="password"
                placeholder="再次确认修改密码"
              />
            </div>
          </List>
        <WingBlank>
          <div className="btn-container">
            <Link to="index/personinfo">
              <Button
                className="btn-next-step" type="primary" onClick={() => {
                }}
              >提交</Button>
            </Link>
          </div>
        </WingBlank>
      </div>
    );
  }
}

const ChangePwdInnerWrapper = createForm()(ChangePwdInner);
export default ChangePwdInnerWrapper;
