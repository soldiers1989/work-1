import React from 'react';
import {createForm} from 'rc-form';
import {List, Picker, InputItem, DatePicker, TextareaItem, ImagePicker, WingBlank, Button} from 'antd-mobile';
import moment from 'moment';
import 'moment/locale/zh-cn';
import Qs from 'qs';
import axios from 'axios';
import config from '../../../config';
import './PropertyRepair.less';

const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('1900-01-01 +0800', 'YYYY-MM-DD Z').utcOffset(8);

const image = [];

var parkId = [];
var buildingTemp = [];
var repairType = [];
var repairTypeTemp = [];


class ServiceRepairForm extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      userInfo: [],
      files: image,
    };
  }
  componentWillMount () {
    //从缓存中读取用户个人信息
    var userInfo = JSON.parse(sessionStorage.userInfo);
    this.setState({
      userInfo : userInfo
    })

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
    var proUrl = config.parentIdExceptTopUrl;
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
        var createDate = date.toISOString().slice(0,19).replace("T"," ").replace("-","/").replace("-","/");
        console.log(createDate);
        var fixPhone = repairInfo.fixPhone.replace(" ","").replace(" ","");
        console.log(fixPhone);
        var company = repairInfo.company;
        var phone = repairInfo.phone.replace(" ","").replace(" ","");
        console.log(phone);
        var parkId = repairInfo.area[0];
        var buildingId = repairInfo.area[1];
        var address = repairInfo.address;
        var repairType = repairInfo.repairType[0];
        var repairTypeConfm = repairInfo.repairType[1];
        var description = repairInfo.description;
        var memo = repairInfo.memo;
        var typeId = 1;
        //从缓存中读取
        var userInfo = sessionStorage.userInfo;
        //json转换为Object对象
        var  reData = JSON.parse(userInfo);
        //读取用户ID
        // console.log(reData.username);
        var applicant = reData.username;
        var data = {
          repairType: repairType,
          company: company,
          description:description,
          address:address,
          parkId:parkId,
          buildingId:buildingId,
          repairTypeConfm:repairTypeConfm,
          contact:fixPhone,
          mobile:phone,
          applicant:applicant,
          memo:memo,
          createDate:createDate,
          typeId:typeId,
          acceptDate:createDate,
          voice_url:'',
          appointDate:createDate,
          descriptionConfm:'',
          photo_url:''
        };
        console.log(data);
        axios.post(config.userRepairUrl,Qs.stringify(data)).then(function(response){//从配置文件中读取url，GET请求
          console.log("userRepairUrl response",response);
          if(response.data.success){
            window.location.href="#/index/Index";
          }else{
            alert(response.msg);
          }
        });
      } else {
        alert('校验失败');
      }
    });
  }
  render() {
    const {getFieldProps} = this.props.form;
    const {files,userInfo} = this.state;
    return (
      <form className="PropertyRepair_div">
        <List renderHeader={() => '基本信息'}>
          <InputItem className="server-list-item"
                     {...getFieldProps('phone',{
                       // initialValue: userInfo.phone,
                     })}
                     clear
                     type="phone"
                     placeholder="186 1234 1234"
          >手机号码</InputItem>
          <InputItem className="server-list-item"
                     {...getFieldProps('company',{
                       initialValue: userInfo.enterpriseInput,
                     })}
                     clear
                     placeholder="请输入公司名称"
                     autoFocus
          >公司名称</InputItem>
          <InputItem className="server-list-item"
                     {...getFieldProps('fixPhone')}
                     type="number"
                     maxLength="8"
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
        <List renderHeader={() => '报修照片'}>
          <ImagePicker
            files={files}
            onChange={this.onChange}
            onImageClick={(index, fs) => console.log(index, fs)}
            selectable={files.length < 5}
          />
        </List>
        <WingBlank>
          <div className="PropertyRepair_div_btn">
            <Button
              className="PropertyRepair_btn" type="primary" inline onClick={this.onSubmit}
            >提交</Button>
          </div>
        </WingBlank>
      </form>
    );
  }
}
const ServiceRepairBar = createForm()(ServiceRepairForm);
export default ServiceRepairBar;
