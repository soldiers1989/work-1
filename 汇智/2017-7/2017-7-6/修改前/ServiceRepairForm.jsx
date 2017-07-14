import React from 'react';
import {createForm} from 'rc-form';
import {List, Picker, InputItem, DatePicker, TextareaItem, ImagePicker} from 'antd-mobile';
import moment from 'moment';
import 'moment/locale/zh-cn';

const zhNow = moment().locale('zh-cn').utcOffset(8);
const maxDate = moment('2017-06-29 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const minDate = moment('1900-01-01 +0800', 'YYYY-MM-DD Z').utcOffset(8);
const park = [{label: '祖冲之路', value: '祖冲之路'}, {label: '金京路', value: '金京路'}];
const floor = [{label: 'A座', value: 'A座'}, {label: 'B座', value: 'B座'}, {label: 'C座', value: 'C座'}];
const repair = [{label: '照明', value: '照明'}, {label: '灯光', value: '灯光'}, {label: '其他', value: '其他'}];
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

  render() {
    const {getFieldProps} = this.props.form;
    const {files} = this.state;
    return (
      <div>
        <List renderHeader={() => '基本信息'}>
          <InputItem
            {...getFieldProps('phone')}
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
          <Picker data={park} cols={1} {...getFieldProps('park')} className="forss">
            <List.Item arrow="horizontal">园区</List.Item>
          </Picker>
          <Picker data={floor} cols={1} {...getFieldProps('floor')} className="forss">
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
          <Picker data={repair} cols={1} {...getFieldProps('repair')} className="forss">
            <List.Item arrow="horizontal">报修类别</List.Item>
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
            {...getFieldProps('district9')}
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
            {...getFieldProps('district9')}
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
      </div>
    );
  }
}
const ServiceRepairBar = createForm()(ServiceRepairForm);
export default ServiceRepairBar;
