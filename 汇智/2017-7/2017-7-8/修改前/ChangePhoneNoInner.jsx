import { List, InputItem, WingBlank, Button } from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import './contact.less';

// 联系方式修改
class ChangePhoneNoInner extends React.Component {
  render() {
    const { getFieldProps } = this.props.form;
    return (
      <div>
          <List className="contact-as-list">
            <InputItem className="contact-list-item"
              {...getFieldProps('newPhoneNo')}
              placeholder="请输入新的手机号"
            />
          </List>
          <Button
            className="contact-btn-getCode" type="primary" inline size="small" onClick={() => {
          }}
          >获取验证码</Button>
          <List className="contact-input-code">
            <InputItem className="contact-list-item"
              {...getFieldProps('code')}
              placeholder="请输入验证码"
            />
          </List>
        <WingBlank>
          <div className="contact-btn-container">
            <Link to="index/personinfo">
              <Button
                className="contact-btn-next-step" type="primary" onClick={() => {
                }}
              >确定</Button>
            </Link>
          </div>
        </WingBlank>
      </div>
    );
  }
}

const ChangePhoneNoInnerWrapper = createForm()(ChangePhoneNoInner);
export default ChangePhoneNoInnerWrapper;
