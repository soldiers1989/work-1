import {List, InputItem, WingBlank, Checkbox, Card, Button,Modal} from 'antd-mobile';
import {createForm} from 'rc-form';
import React from 'react';
import {Link} from 'react-router';

import request from '../../../utils/requestGET';
import config from '../../../config';
const AgreeItem = Checkbox.AgreeItem;
const alert = Modal.alert;
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
  /*----------表单验证------------*/

  /*身份证号验证*/
  validateIdCard = (rule, value, callback) => {
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;

    if(!value || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(value)){
      tip = "身份证号格式错误";
      pass = false;
    }

    else if(!city[value.substr(0,2)]){
      tip = "地址编码错误";
      pass = false;
    }
    else{
      //18位身份证需要验证最后一位校验位
      if(value.length == 18){
        value = value.split('');
        //∑(ai×Wi)(mod 11)
        //加权因子
        var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
        //校验位
        var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
        var sum = 0;
        var ai = 0;
        var wi = 0;
        for (var i = 0; i < 17; i++)
        {
          ai = value[i];
          wi = factor[i];
          sum += ai * wi;
        }
        var last = parity[sum % 11];
        if(parity[sum % 11] != value[17]){
          tip = "校验位错误";
          pass =false;
        }
      }
    }
    if(!pass){
      callback(new Error(tip));
    };
    callback();
  }

  /*邮箱验证*/
  validateEmail = (rule, value, callback) => {
    var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    if(re.test(value)){
      callback();
    }else{
      callback(new Error('邮箱格式错误！'));
    }
  }
/*手机号验证*/
  validatePhone = (rule, value, callback) => {
    var re = /^1[34578]\d{9}$/;
    if(re.test(value)){
      callback();
    }else{
      callback(new Error('手机号格式错误！'));
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
    const { getFieldProps, getFieldError } = this.props.form;
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
                {...getFieldProps('username',{
                  rules:[
                    { required: true, message: '请输入用户名' },
                    { validator: this.validateUsername },
                  ]
                })}
                clear
                error={!!getFieldError('username')}
                onErrorClick={() => {
                  alert(getFieldError('username').join('、'));
                }}
                maxLength = "20"
                placeholder="请输入用户名"
              ><span className="ApplyCard_label_color">*</span> 申请人</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('email',{
                  rules:[
                    { required: true, message: '请输入邮箱' },
                    { validator: this.validateEmail },
                  ]
                })}
                clear
                error={!!getFieldError('email')}
                onErrorClick={() => {
                  alert(getFieldError('email').join('、'));
                }}
                placeholder="请输入邮箱"
              ><span className="ApplyCard_label_color">*</span> E-mail</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('company',{
                  rules:[
                    { required: true, message: '请输入公司名称' },
                    { validator: this.validateCompany },
                  ]
                })}
                clear
                error={!!getFieldError('company')}
                onErrorClick={() => {
                  alert(getFieldError('company').join('、'));
                }}
                maxLength = "100"
                placeholder="请输入公司名称"
              ><span className="ApplyCard_label_color">*</span> 公司名称</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('phone',{
                  rules:[
                    { required: true, message: '请输入手机号' },
                    { validator: this.validatePhone },
                  ]
                })}
                clear
                error={!!getFieldError('phone')}
                onErrorClick={() => {
                  alert(getFieldError('phone').join('、'));
                }}
                type="number"
                maxLength="11"
                placeholder="请输入手机号"
              ><span className="ApplyCard_label_color">*</span> 联系方式</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('realname',{
                  rules:[
                    { required: true, message: '请输入真实姓名' },
                    { validator: this.validateRealname },
                  ]
                })}
                clear
                error={!!getFieldError('realname')}
                onErrorClick={() => {
                  alert(getFieldError('realname').join('、'));
                }}
                maxLength = "20"
                placeholder="请输入真实姓名"
              ><span className="ApplyCard_label_color">*</span> 真实姓名</InputItem>
            </List>
            <List className="ApplyCard_List">
              <InputItem
                className="ApplyCard_InputItem"
                {...getFieldProps('IdCard',{
                  rules:[
                    { required: true, message: '请输入身份证号' },
                    { validator: this.validateIdCard },
                  ]
                })}
                clear
                error={!!getFieldError('IdCard')}
                onErrorClick={() => {
                  alert(getFieldError('IdCard').join('、'));
                }}
                placeholder="请输入身份证号"
                maxLength = "18"
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
