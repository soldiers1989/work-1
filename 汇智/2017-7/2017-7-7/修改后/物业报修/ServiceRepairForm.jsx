import React from 'react';
import {createForm} from 'rc-form';
import {List, Picker, InputItem, DatePicker, TextareaItem, ImagePicker, WingBlank, Button} from 'antd-mobile';
import moment from 'moment';
import 'moment/locale/zh-cn';
import { Link } from 'react-router';
import request from '../../../utils/request';
import config from '../../../config';

const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('1900-01-01 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const park = [{label: '祖冲之路', value: '祖冲之路'}, {label: '金京路', value: '金京路'}];
const floor = [{label: 'A座', value: 'A座'}, {label: 'B座', value: 'B座'}, {label: 'C座', value: 'C座'}];
const repair = [{label: '照明', value: '照明'}, {label: '灯光', value: '灯光'}, {label: '其他', value: '其他'}];
const repairChildType = [{label:'其他', value:'其他'}, {label: '灯光', value: '灯光'}];
const image = [];

class ServiceRepairForm extends React.Component {
  state = {
    files: image,
  }
  onChange = (files, type, index) => {
    console.log(files, type, index);
    this.setState({
      files,
    });
  }
  onSubmit = () => {
    this.props.form.validateFields({ force: true }, (error) => {
      if (!error) {
        var repairInfo = this.props.form.getFieldsValue();
        var contact = repairInfo.contact;
        var company = repairInfo.company;
        var mobile = repairInfo.fixedPhone;
        var parkId = repairInfo.parkId;
        var buildingId = repairInfo.buildingId;
        var address = repairInfo.address;
        var repairTypeParent = repairInfo.repairTypeParent;
        var repairType = repairInfo.repairType;
        var description = repairInfo.description;
        var memo = repairInfo.memo;

        //从缓存中读取
        var userInfo = sessionStorage.obj;
        //json转换为Object对象
        var  reData = JSON.parse(userInfo);
        //读取用户ID
        // console.log(reData.username);
        var applicant = reData.username;
        var params = "contact="+contact+"&company="+company+"&mobile="+mobile+"&parkId="+parkId+"&buildingId="
          +buildingId+"&address="+address+"&repairTypeParent="+repairTypeParent+"&repairType="+repairType+"&description="
        +description+"&memo="+memo+"&applicant="+applicant;
        // console.log(repairInfo);
        // console.log(params);
        //post请求
        // var url = config.userRepairUrl;
        // console.log(url);
        request(config.userRepairUrl,params).then((data) => {//从配置文件中读取url
          console.log(data);
          // var data1 = data.msg;
          // if(data1.success){
          //   alert(data1.msg);
          // }else{
          //   alert(data1.msg);
          // }
        });
      } else {
        alert('校验失败');
      }
    });
  }
  render() {
    const {getFieldProps} = this.props.form;
    const {files} = this.state;
    return (
      <form>
        <List renderHeader={() => '基本信息'}>
          <InputItem
            {...getFieldProps('contact')}
            type="phone"
            placeholder="186 1234 1234"
          >手机号码</InputItem>
          <InputItem
            {...getFieldProps('company')}
            clear
            placeholder="请输入公司名称"
            autoFocus
          >公司名称</InputItem>
          <InputItem
            {...getFieldProps('fixedPhone')}
            type="phone"
          >固定电话</InputItem>
        </List>

        <List renderHeader={() => '区域信息'}>
          <Picker data={park} cols={1} {...getFieldProps('parkId')} className="forss">
            <List.Item arrow="horizontal">园区</List.Item>
          </Picker>
          <Picker data={floor} cols={1} {...getFieldProps('buildingId')} className="forss">
            <List.Item arrow="horizontal">楼宇</List.Item>
          </Picker>
          <InputItem
            {...getFieldProps('address')}
            clear
            placeholder="如：金京路1139路A座"
            autoFocus
          >地址详情</InputItem>
        </List>

        <List renderHeader={() => '报修信息'}>
          <Picker data={repair} cols={1} {...getFieldProps('repairTypeParent')} className="forss">
            <List.Item arrow="horizontal">报修类别</List.Item>
          </Picker>
          <Picker data={repairChildType} cols={1} {...getFieldProps('repairType')} className="forss">
            <List.Item arrow="horizontal">报修子类</List.Item>
          </Picker>
          <DatePicker
            mode="date"
            title="选择日期"
            extra="请选择"
            {...getFieldProps('date1', {
              initialValue: zhNow,
            })}
            minDate={minDate}
            maxDate={maxDate}
          >
            <List.Item arrow="horizontal">报修时间</List.Item>
          </DatePicker>
          <ImagePicker
          files={files}
          onChange={this.onChange}
          onImageClick={(index, fs) => console.log(index, fs)}
          selectable={files.length < 5}
          />
          <TextareaItem
            title="报修描述"
            {...getFieldProps('description')}
            placeholder="100字以内"
            data-seed="logId"
            autoHeight
            focused={this.state.focused}
            onFocus={() => {
              this.setState({
                focused: false,
              });
            }}
          />
          <TextareaItem
            title="备注"
            {...getFieldProps('memo')}
            placeholder="100字以内"
            data-seed="logId"
            autoHeight
            focused={this.state.focused}
            onFocus={() => {
              this.setState({
                focused: false,
              });
            }}
          />
        </List>
        <WingBlank>
          <div className="btn-container" style={{ marginTop: '10px' }}>
            <Button onClick={this.onSubmit} inline
                    className="btn" type="primary"
                    style={{
                      background: '#259dda',
                      fontSize: '1em',
                    }}
            >提交</Button>
          </div>
        </WingBlank>
      </form>
    );
  }
}
const ServiceRepairBar = createForm()(ServiceRepairForm);
export default ServiceRepairBar;
