import React from 'react';
import { createForm } from 'rc-form';
import {List, Picker, InputItem, DatePicker, TextareaItem, ImagePicker, WingBlank, Button} from 'antd-mobile';
import { Link } from 'react-router';
import moment from 'moment';
import 'moment/locale/zh-cn';
import Qs from 'qs';
import axios from 'axios';
import config from '../../../config';
import './ITRepair.less'
const repairChildType = [{label:'其他', value:'其他'}, {label: '灯光', value: '灯光'}];
const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('1900-01-01 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const park = [{ label: '祖冲之路', value: '祖冲之路' }, { label: '金京路', value: '金京路' }];
const floor = [{ label: 'A座', value: 'A座' }, { label: 'B座', value: 'B座' }, { label: 'C座', value: 'C座' }];
const repair = [{ label: '照明', value: '照明' }, { label: '灯光', value: '灯光' }, { label: '其他', value: '其他' }];
const image = [];

var parkId = [];
var buildingTemp = [];
var repairType = [];
var repairTypeTemp = [];


class ITRepairForm extends React.Component {
  state = {
    files: image,
  }
  componentWillMount () {
    /*获取园区和楼宇信息*/
    var proUrl = config.parkUrl;
    axios.get(proUrl).then(function(response){//一级
      const proList = response.data.result;
      var proData = "";
      for (var i=0; i<proList.length; i++){
        proData = proList[i];
        const proObj={};
        proObj.label = proData.name;
        proObj.value = proData.objectid;
        axios.get(config.buildingUrl.replace("{ParentId}",proData.objectid)).then(function(response){//二级
          buildingTemp = [];
          var buildingList = response.data.result;
          var buildingData = "";
          for (var j=0; j<buildingList.length; j++) {
            buildingData = buildingList[j];
            var buildingObj = {};
            buildingObj.label = buildingData.name;
            buildingObj.value = buildingData.objectid;
            buildingTemp.push(buildingObj);
          }
          proObj.children = buildingTemp;
          parkId.push(proObj);
        });
      }
    });
    /*获取报修信息*/
    var proUrl = config.ITrepairsUrl;
    axios.get(proUrl).then(function(response){//一级
      const proList = response.data.result;
      var proData = "";
      for (var i=0; i<proList.length; i++){
        proData = proList[i];
        const proObj={};
        proObj.label = proData.name;
        proObj.value = proData.objectid;
        axios.get(config.settingDictUrl.replace("{ParentId}",proData.objectid)).then(function(response){//二级
          repairTypeTemp = [];
          var repairTypeList = response.data.result;
          var repairTypeData = "";
          for (var j=0; j<repairTypeList.length; j++) {
            repairTypeData = repairTypeList[j];
            var repairTypeobj = {};
            repairTypeobj.label = repairTypeData.name;
            repairTypeobj.value = repairTypeData.objectid;
            repairTypeTemp.push(repairTypeobj);
          }
          proObj.children = repairTypeTemp;
          repairType.push(proObj);
        });
      }
    });
  }
  componentDidMount(){
    this.setState({
      park : parkId,
      repairType: repairType,
    })
  }
  _onChange(evt){
    this.setState({
      value: evt.target.value
    })
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
        console.log(repairInfo);
        var date = repairInfo.date1._d;
        date = date.toISOString().slice(0,10);
        console.log(date);
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
          +description+"&memo="+memo+"&applicant="+applicant+"&date="+date;
        // console.log(repairInfo);
        // console.log(params);
        //post请求
        // var url = config.userRepairUrl;
        // console.log(url);
        request(config.ITRepairUrl,params).then((data) => {//从配置文件中读取url
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
    const { getFieldProps } = this.props.form;
    const { files } = this.state;
    return (
      <form className="ITRepair_div">
        <List renderHeader={() => '基本信息'} >
          <InputItem className="server-list-item"
            {...getFieldProps('contact')}
            type="phone"
            placeholder="186 1234 1234"
          >手机号码</InputItem>
          <InputItem className="server-list-item"
            {...getFieldProps('company')}
            clear
            placeholder="请输入公司名称"
            autoFocus
          >公司名称</InputItem>
          <InputItem className="server-list-item"
            {...getFieldProps('fixedPhone')}
            type="phone"
          >固定电话</InputItem>
        </List>

        <List renderHeader={() => '区域信息'}>
          <Picker extra="请选择(可选)" cols={2}
                  data={parkId}
                  title="选择园区和楼宇"
                  {...getFieldProps('area')}
                  onOk={e => console.log('ok', e)}
                  onDismiss={e => console.log('dismiss', e)}
          >
            <List.Item arrow="horizontal">园区/楼宇</List.Item>
          </Picker>
          <InputItem
            {...getFieldProps('address')}
            clear
            placeholder="如：金京路1139路A座"
            autoFocus
          >地址详情</InputItem>
        </List>

        <List renderHeader={() => '报修信息'}>
          <Picker extra="请选择(可选)" cols={2}
                  data={repairType}
                  title="选择报修类别和子类"
                  {...getFieldProps('repairType')}
                  onOk={e => console.log('ok', e)}
                  onDismiss={e => console.log('dismiss', e)}
          >
            <List.Item arrow="horizontal">报修类别/子类</List.Item>
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
        <List renderHeader={() => '报修照片'} className="ITRepair_photo">
          <ImagePicker
            files={files}
            onChange={this.onChange}
            onImageClick={(index, fs) => console.log(index, fs)}
            selectable={files.length < 5}
          />
        </List>
        <div className="ITRepair_div_btn">
            <Button
              className="ITRepair_btn" type="primary" inline onClick={this.onSubmit}
            >提交</Button>
        </div>
      </form>
    );
  }
}
const ITRepairBar = createForm()(ITRepairForm);
export default ITRepairBar;
