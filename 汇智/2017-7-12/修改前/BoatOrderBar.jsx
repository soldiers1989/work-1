import React from 'react';
import {List, InputItem, DatePicker} from 'antd-mobile';
import {createForm} from 'rc-form';
import {Link} from 'react-router';
import moment from 'moment';
import 'moment/locale/zh-cn';
import './boatOrder.less';

const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2020-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);

const maxTime = moment('14:00 +0800', 'HH:mm Z').utcOffset(8);
const minTime = moment('01:00 +0800', 'HH:mm Z').utcOffset(8);
const Item = List.Item;

// 游船预约
class BoatOrderBar extends React.Component {
  onSubmit = () => {
    this.props.form.validateFields({ force: true }, (error) => {
      if (!error) {
        var orderInfo = this.props.form.getFieldsValue();

        var username = orderInfo.username;
        var company = orderInfo.company;
        var mobile = orderInfo.mobile;
        var dingStartTime = orderInfo.dingStartTime;
        var dingNumber = orderInfo.dingNumber;
        var code = orderInfo.code;

        var params = "username=" + username + "&company=" + company
          + "&mobile=" + mobile + "&dingStartTime=" + dingStartTime
          + "&dingNumber=" + dingNumber + "&code=" + code;
        //post请求
        request(config.boatOrder,params).then((data) => {//从配置文件中读取url
          var reData = data.msg;
          if(reData.success) {//成功

            //跳转页面
            window.location.href="#index/Index";
          }
        });
      }else{//游船验证失败
        alert(reData.msg);
        this.props.form.resetFields();
      }
    });
  }

  render() {
    const {getFieldProps} = this.props.form;
    return (
      <form>
        <List className="date-picker-list">
          <InputItem className="server-list-item"
                     {...getFieldProps('username')}
                     clear
                     placeholder="请输入预约人"
                     autoFocus
          >用户名</InputItem>

          <InputItem
            {...getFieldProps('company')}
            clear
            placeholder="请输入所属公司"
            autoFocus
          >所属公司</InputItem>

          <InputItem
            {...getFieldProps('mobile')}
            type="phone"
            placeholder="186 1234 1234"
          >手机号码</InputItem>

          <DatePicker
            mode="date"
            title="选择日期"
            extra="请选择"
            {...getFieldProps('date', {
              initialValue: zhNow,
            })}
            minDate={minDate}
            maxDate={maxDate}
          >
            <List.Item arrow="horizontal">预约日期</List.Item>
          </DatePicker>
          <DatePicker
            mode="time"
            {...getFieldProps('startTime', {
              initialValue: zhNow,
            })}
            minDate={minTime}
            maxDate={maxTime}
          >
            <List.Item arrow="horizontal">时间(CST)，限定上下限</List.Item>
          </DatePicker>
          <DatePicker
            mode="time"
            {...getFieldProps('endTime', {
              initialValue: zhNow,
            })}
            minDate={minTime}
            maxDate={maxTime}
          >
            <List.Item arrow="horizontal">时间(CST)，限定上下限</List.Item>
          </DatePicker>
          <InputItem
            {...getFieldProps('dingNumber', {
              initialValue: '1',
            })}
            clear
            placeholder="请输入数量"
            autoFocus
          >预约数量</InputItem>

          <List className="verify-code-input">
            <InputItem
              {...getFieldProps('code')}
              placeholder="请输入验证码"
            />
          </List>
          <div className="verify-code-right">
            <Link>
              <img
                src="http://222.73.203.71:8080/WiseAuth/AuthImageServlet" className="verify-code-image"
                style={{}} alt="image"
                onClick={() => {
                }}
              />

            </Link>
          </div>
          <List>
            <Item>
              <div className="boatOrder-blank"/>
            </Item>
          </List>

        </List>
      </form>
    );
  }
}
const BoatOrderBarWrap = createForm()(BoatOrderBar);
export default BoatOrderBarWrap;
