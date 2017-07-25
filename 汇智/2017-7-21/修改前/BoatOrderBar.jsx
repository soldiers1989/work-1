import React from 'react';
import {List, InputItem, DatePicker,WingBlank,Button,Modal} from 'antd-mobile';
import {createForm} from 'rc-form';
import {Link} from 'react-router';
import moment from 'moment';
import 'moment/locale/zh-cn';
import './boatOrder.less';
import config from '../../../config';
import requestGET from '../../../utils/requestGET';
import request from '../../../utils/requestPOST';
import axios from 'axios';
import Qs from 'qs';

const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2020-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);

const maxTime = moment('14:00 +0800', 'HH:mm Z').utcOffset(8);
const minTime = moment('10:00 +0800', 'HH:mm Z').utcOffset(8);
const Item = List.Item;
const alert=Modal.alert;

// 游船预约
class BoatOrderBar extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      imgUrl: config.codeImgUrl,
      userInfo: [],
    };
  }
  /*验证手机号*/
  validateMobile = (rule, value, callback) => {
    this.focus;
    value = value.replace(" ","").replace(" ","");
    var re = /^1[34578]\d{9}$/;
    if(re.test(value)){
      callback();
    }else{
      callback(new Error('手机号格式错误！'));
    }
  }
  changeImg = () =>{
    var nowDate = new Date().getTime();
    console.log(nowDate);
    var ImgUrl = config.codeImgUrl + "?" +nowDate;
    this.setState({
      imgUrl : ImgUrl
    })
  }
  componentWillMount () {
    //从缓存中读取用户个人信息
    var userInfo = JSON.parse(localStorage.userInfo);
    this.setState({
      userInfo : userInfo
    })
  }
  onSubmit = () => {
    this.props.form.validateFields({ force: true }, (error) => {
      if (!error) {
        var orderInfo = this.props.form.getFieldsValue();

        var username = orderInfo.username;
        var company = orderInfo.company;
        var mobile = orderInfo.mobile;
        // var dingStartTime = orderInfo.dingStartTime;
        var dingNumber = orderInfo.dingNumber;
        var code = orderInfo.code;

        var dingDate = orderInfo.dingDate._d;
        dingDate = dingDate.toISOString().slice(0,10);


        var startTime = orderInfo.startTime._d;
        startTime = startTime.toISOString().slice(11,16);

        var endTime = orderInfo.endTime._d;
        endTime = endTime.toISOString().slice(11,16);


        // var params = "username=" + username + "&company=" + company
        //   + "&mobile=" + mobile + "&dingStartTime=" + dingStartTime
        //   + "&dingNumber=" + dingNumber + "&code=" + code;
        var orderData = {
          email:this.state.userInfo.email,
          username: username,
          company: company,
          mobile: mobile,
          dingStartTime:dingDate+" "+startTime+":00",
          dingEndTime:dingDate+" "+endTime+":00",
          dingNumber:dingNumber,
          name:this.state.userInfo.realName,
        };
        console.log(orderData);
        //post请求
        // request(config.boatOrder,params).then((data) => {//从配置文件中读取url
        //   var reData = data.msg;
        axios.post(config.boatOrder,Qs.stringify(orderData)).then(function(response){//从配置文件中读取url，POST请求
          var reData = response.data;
          console.log(reData);
          if(reData.success) {//成功
            alert("预约成功！", '', [
              { text: '确认', onPress: () => window.location.href="#index/Index", style: {fontWeight: 'bold'} },
            ]);
            console.log("6666666");
            //跳转页面
            // window.location.href="#index/Index";
          }
        });
      }else{//游船验证失败
        alert(reData.msg);
        this.props.form.resetFields();
      }
    });
  }

  render() {
    const { getFieldProps, getFieldError } = this.props.form;
    const {imgUrl,userInfo} = this.state;
    return (
      <form>
        <List className="date-picker-list">
          <InputItem className="boatOrder-list-item"
                     {...getFieldProps('username',{
                       initialValue: userInfo.username,
                       rules: [
                         { required: true, message: '请输入预约人' },
                       ],
                     })}
                     clear
                     placeholder="请输入预约人"
                     autoFocus
                     error={!!getFieldError('username')}
                     onErrorClick={() => {
                       alert(getFieldError('username').join('、'));
                     }}
          ><span className="ApplyCard_label_color">*</span>用户名</InputItem>

          <InputItem className="boatOrder-list-item"
            {...getFieldProps('company',{
              initialValue: userInfo.enterpriseInput,
              rules: [
                { required: true, message: '请输入所属公司' },
              ],
            })}
            clear
            placeholder="请输入所属公司"
            autoFocus
                     error={!!getFieldError('company')}
                     onErrorClick={() => {
                       alert(getFieldError('company').join('、'));
                     }}
          ><span className="ApplyCard_label_color">*</span>所属公司</InputItem>

          <InputItem className="boatOrder-list-item"
            {...getFieldProps('mobile',{
              initialValue: userInfo.phone,
              rules: [
                { required: true, message: '请输入手机号' },
                { validator: this.validateMobile },
              ],
            })}
            clear
            type="number"
                     maxLength = "11"
            placeholder="18612341234"
                     error={!!getFieldError('mobile')}
                     onErrorClick={() => {
                       alert(getFieldError('mobile').join('、'));
                     }}
          ><span className="ApplyCard_label_color">*</span>手机号码</InputItem>

          <DatePicker
            className="boatOrder_DatePicker"
            mode="date"
            title="选择日期"
            extra="请选择"
            {...getFieldProps('dingDate', {
              initialValue: zhNow,
            })}
            minDate={minDate}
            maxDate={maxDate}
          >
            <List.Item arrow="horizontal"   className="boatOrder_DatePicker">
              <span className="ApplyCard_label_color">*</span>预约日期</List.Item>
          </DatePicker>
          <DatePicker
            className="boatOrder_DatePicker"
            mode="time"
            {...getFieldProps('startTime', {
              initialValue: zhNow,
            })}
            minDate={minTime}
            maxDate={maxTime}
          >
            <List.Item arrow="horizontal"  className="boatOrder_DatePicker">
              <span className="ApplyCard_label_color">*</span>预约时间（开始时间）</List.Item>
          </DatePicker>
          <DatePicker
            className="boatOrder_DatePicker"
            mode="time"
            {...getFieldProps('endTime', {
              initialValue: zhNow,
            })}
            minDate={minTime}
            maxDate={maxTime}
          >
            <List.Item arrow="horizontal"  className="boatOrder_DatePicker">
              <span className="ApplyCard_label_color">*</span>预约时间（结束时间）</List.Item>
          </DatePicker>
          <InputItem className="boatOrder-list-item"
            {...getFieldProps('dingNumber', {
              initialValue: '1',
              rules: [
                { required: true, message: '请输入数量' },
              ],
            })}
            clear
            placeholder="请输入数量"
            autoFocus
                     error={!!getFieldError('dingNumber')}
                     onErrorClick={() => {
                       alert(getFieldError('dingNumber').join('、'));
                     }}
          ><span className="ApplyCard_label_color">*</span>预约数量</InputItem>

          {/*<List className="verify-code-input">*/}
            {/*<InputItem className="boatOrder-list-item"*/}
              {/*{...getFieldProps('code')}*/}
              {/*placeholder="请输入验证码"*/}
            {/*/>*/}
          {/*</List>*/}
          {/*<div className="verify-code-right">*/}
            {/*<Link>*/}
              {/*<img*/}
                {/*src={imgUrl} className="verify-code-image"*/}
                {/*alt="image"*/}
                {/*onClick={this.changeImg}*/}
              {/*/>*/}

            {/*</Link>*/}
          {/*</div>*/}
          {/*<List>*/}
            {/*<Item>*/}
              {/*<div className="boatOrder-blank"/>*/}
            {/*</Item>*/}
          {/*</List>*/}
          <WingBlank>
                <Button
                  className="boatOrder_submit" type="primary" onClick={() => {this.onSubmit()}}
                >提交</Button>
          </WingBlank>
        </List>
      </form>
    );
  }
}
const BoatOrderBarWrap = createForm()(BoatOrderBar);
export default BoatOrderBarWrap;
