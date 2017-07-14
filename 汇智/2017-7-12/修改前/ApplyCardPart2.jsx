import {List, InputItem, WingBlank, Checkbox, Card, Button} from 'antd-mobile';
import {createForm} from 'rc-form';
import React from 'react';
import {Link} from 'react-router';
import request from '../../../utils/request';
import config from '../../../config';
const AgreeItem = Checkbox.AgreeItem;
// 申请汇智卡
class ApplyCardPart2 extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      checked:false,
      disable:false,
    };
  }
  /*------同意须知--------*/
  agreeApply(e){
    if(e.target.checked){
      this.state.disabled=true;
      console.log("同意须知"+this.state.disabled);
    } else{
      this.state.disabled=false;
      console.log(this.state.disabled);
    }
  }
  /*----form提交---*/
  onSubmit = () => {
    var state = this.state.disabled;
    if(state){
      this.props.form.validateFields({ force: true }, (error) => {
        if (!error) {
          var applyInfo = this.props.form.getFieldsValue();
          var username = applyInfo.username;
          var email = applyInfo.email;
          var company = applyInfo.company;
          var phone = applyInfo.phone;
          var realname = applyInfo.realname;
          var IdCard = applyInfo.IdCard;
          var params = "username="+username+"&email="+email+"&company="+company+
            "&phone="+phone+"&realname="+realname+"&IdCard="+IdCard;
          request(config.applyAddUrl,params).then((data) => {//从配置文件中读取url
            var reData = data.msg;
            if(reData.success){//提交成功
              window.location.href="#index/bound";     //已绑定汇智卡
            }else {
              alert('提交失败');
            }
          })
        }
      })
    }else{
      alert("请勾选同意须知");
    }
  };
  render() {
    const {getFieldProps} = this.props.form;
    return (
      <form>
        <div>
          <WingBlank
            style={{
              marginTop: '10px',
            }}
          >
            <AgreeItem
              onChange={(e)=>this.agreeApply(e)}>同意须知</AgreeItem>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('username')}
                placeholder="请输入用户名"
              ><span className="ApplyCard_label_color">*</span> 申请人</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('email')}
                placeholder="请输入邮箱"
              ><span className="ApplyCard_label_color">*</span> E-mail</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('company')}
                placeholder="请输入公司名称"
              ><span className="ApplyCard_label_color">*</span> 公司名称</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('phone')}
                placeholder="请输入手机号"
              ><span className="ApplyCard_label_color">*</span> 联系方式</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('realname')}
                placeholder="请输入真实姓名"
              ><span className="ApplyCard_label_color">*</span> 真实姓名</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('IdCard')}
                placeholder="请输入身份证号"
              ><span className="ApplyCard_label_color">*</span> 身份证</InputItem>
            </List>

            <div className="ApplyCard_div_btn">
              {/*<Link to="index/unbound">*/}
                <Button
                  className="ApplyCard_btn" ref="ApplyCard_btn" type="primary"
                  inline onClick={this.onSubmit}
                >提交</Button>
              {/*</Link>*/}
            </div>
          </WingBlank>
        </div>
      </form>
    );
  }
}

const ApplyCardPart2Wrapper = createForm()(ApplyCardPart2);
export default ApplyCardPart2Wrapper;
