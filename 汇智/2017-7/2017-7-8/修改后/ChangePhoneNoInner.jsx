import { List, InputItem, WingBlank, Button } from 'antd-mobile';
import { createForm } from 'rc-form';
import React from 'react';
import { Link } from 'react-router';
import './contact.less';
import config from '../../../config';
import requestGET from '../../../utils/requestGET';
import request from '../../../utils/request';

// 联系方式修改
class ChangePhoneNoInner extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      imgUrl: config.codeImgUrl
    };
  }

  changeImg = () =>{
    var nowDate = new Date().getTime();
    console.log(nowDate);
    var ImgUrl = config.codeImgUrl + "?" +nowDate;
    this.setState({
      imgUrl : ImgUrl
    })
  }
  render() {
    const {imgUrl} = this.state;
    const { getFieldProps } = this.props.form;
    return (
      <form>
          <List className="contact-as-list">
            <InputItem className="contact-list-item"
              {...getFieldProps('newPhoneNo')}
              placeholder="请输入新的手机号"
            />

          </List>
        <div className="register-verify-code-right">
          <Link>
            <img
              src={imgUrl} className="register-verify-code-image"
              style={{ }} alt="image"
              onClick={this.changeImg}
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
            {/*<Link to="index/personinfo">*/}
              <Button
                className="contact-btn-next-step" type="primary" onClick={{}}
              >确定</Button>
            {/*</Link>*/}
          </div>
        </WingBlank>
      </form>
    );
  }
}

const ChangePhoneNoInnerWrapper = createForm()(ChangePhoneNoInner);
export default ChangePhoneNoInnerWrapper;
