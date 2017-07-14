import { List, Picker, WingBlank, Button, InputItem, DatePicker, TextareaItem, Select, Cascader } from 'antd-mobile';
import React from 'react';
import { createForm } from 'rc-form';
import moment from 'moment';
import 'moment/locale/zh-cn';

const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('1900-01-01 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const seasons = [{ label: '男', value: '男' }, { label: '女', value: '女' }];
const contry = [{ label: '美国', value: '美国' },{ label: '加拿大', value: '加拿大' },
  { label: '中国', value: '中国' }];
const marry = [{ label: '已婚', value: '已婚' }, { label: '未婚', value: '未婚' }];
const study = [{ label: '其他', value: '其他' }, { label: '博士研究生', value: '博士研究生' },
  { label: '硕士研究生', value: '硕士研究生' }, { label: '本科', value: '本科' },
  { label: '大专', value: '大专' },{ label: '高中', value: '高中' },
  { label: '初中', value: '初中' },{ label: '小学及以下', value: '小学及以下' }];
const dis = [[{ label: '上海', value: '上海' }, { label: '浙江', value: '浙江' }],
  [{ label: '上海市', value: '上海市' }, { label: '杭州', value: '杭州' }]];
//
class UserInfo1 extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      userInfo: []
    };
  }
  componentWillMount () {
    //从缓存中读取用户个人信息
    var userInfo = JSON.parse(sessionStorage.obj);
    this.setState({
      userInfo : userInfo
    })
    console.log(userInfo);
  }
  //提交
  onSubmit = () => {
    this.props.form.validateFields({ force: true }, (error) => {
      if (!error) {
        var userInfo = this.props.form.getFieldsValue();
        // var

        //post请求
        request(config.loginUrl,params).then((data) => {//从配置文件中读取url
          var reData = data.msg;
          if(reData.success){//提交成功

          }else{//提交失败
            alert(reData.msg);
            // this.props.form.resetFields();//重置表单的值
          }
        });
      } else {
        alert('校验失败');
      }
    });
  }
  state = {
    value: null,
  };
  onChange = (value) => {
    console.log(value);
    this.setState({
      value,
    });
  };
  onClick = () => {
    setTimeout(() => {
      this.setState({
        data: province,
      });
    }, 120);
  };
  render() {
    const { getFieldProps } = this.props.form;
    const {userInfo} = this.state;
    return (
      <form>
        <div>
          <List
            className="date-picker-list"
            style={{ backgroundColor: 'white' }}
          >
          <InputItem
            {...getFieldProps('userName')}
            clear
            placeholder="请输入用户名"
            autoFocus
            value ={userInfo.username}
          >用户名</InputItem>

          <InputItem
            {...getFieldProps('nickName', {
              initialValue: 'userInfo.username',
            })}
            clear
            placeholder="请输入昵称"
            autoFocus
            // value={userInfo.username}
          >昵称</InputItem>

          <Picker data={seasons} cols={1} {...getFieldProps('sex')} className="forss">
            <List.Item arrow="horizontal">性别</List.Item>
          </Picker>
          <InputItem
            {...getFieldProps('realName', {
              initialValue: '88',
            })}
            clear
            placeholder="请输入真实姓名"
            maxLength={10}
            autoFocus
            // value ={userInfo.realName}
          >真实姓名</InputItem>
          <DatePicker
            mode="date"
            title="选择日期"
            extra="请选择"
            {...getFieldProps('birthDay', {
              initialValue: zhNow,
            })}
            minDate={minDate}
            maxDate={maxDate}
          >
            <List.Item arrow="horizontal">出生日期</List.Item>
          </DatePicker>
          <InputItem
            {...getFieldProps('workYear', {
              initialValue: '8',
            })}
            clear
            placeholder="请输入工作年限"
            type = "number"
            autoFocus
          >工作年限</InputItem>
          <Picker data={contry} cols={1} {...getFieldProps('nation')} className="forss">
            <List.Item arrow="horizontal">国籍</List.Item>
          </Picker>
          <Picker
            data={dis}
            title="选择地区"
            {...getFieldProps('area')}
            cascade={false}
            extra="请选择"
            value={this.state.sValue}
            onChange={v => this.setState({ sValue: v })}
          >
            <List.Item arrow="horizontal">户籍地</List.Item>
          </Picker>
          <Picker
            data={dis}
            title="选择地区"
            {...getFieldProps('residence')}
            cascade={false}
            extra="请选择"
            value={this.state.sValue}
            onChange={v => this.setState({ sValue: v })}
          >
            <List.Item arrow="horizontal">居住地</List.Item>
          </Picker>
          <InputItem
            {...getFieldProps('companyName')}
            clear
            placeholder="请输入企业名称"
            autoFocus
          >企业名称</InputItem>

          <Picker data={marry} cols={1} {...getFieldProps('isMarry')} className="forss">
            <List.Item arrow="horizontal">婚否</List.Item>
          </Picker>
          <Picker data={study} cols={1} {...getFieldProps('education')} className="forss">
            <List.Item arrow="horizontal">学历</List.Item>
          </Picker>
          <TextareaItem
            title="个性签名"
            {...getFieldProps('perSignature')}
            placeholder="请输入个性签名"
            data-seed="logId"
            maxLength="140"
            autoHeight
            focused={this.state.focused}
            onFocus={() => {
              this.setState({
                focused: false,
              });
            }}
          />
        </List>
          <WingBlank className="login-wingBlank">
            <div className="btn-container">
              <Button
                className="btn-login" type="primary" onClick={this.onSubmit} inline
              >提交</Button>
            </div>
          </WingBlank>
      </div>
    </form>
    );
  }
}
const UserInfoWrapper =  createForm()(UserInfo1);
export default UserInfoWrapper;
