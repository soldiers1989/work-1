import {WingBlank, SegmentedControl, Checkbox, Picker, List, Button, Tag, Modal} from 'antd-mobile';
import {createForm} from 'rc-form';
import React from 'react';
import {Link} from 'react-router';
import axios from 'axios';
import requestGET from '../../../utils/requestGET';

import './recharge1.less';
import config from '../../../config';
const Item = List.Item;
const AgreeItem = Checkbox.AgreeItem;
const alert = Modal.alert;
//可用红包列表
var validInfoList = [];
//红包列表
var RedPackets = [];
// var money = 50;
//折扣金额
var discount = 0;
// 充值
class RechargePart2 extends React.Component {
  state = {
    checked: false,
    disabled: false,
    money : 50,
    reMoney:50,
    RedPacketId:'',
    selectedIndex:0,
    haveRedpacket:true,
    userInfo: [],
  }

  componentWillMount () {
    //从缓存中读取用户信息
    if(localStorage.userInfo != undefined){
      var userInfo = JSON.parse(localStorage.userInfo);
      this.setState({
        userInfo : userInfo,
      })
    }
  }

  componentDidMount () {
    requestGET(config.validRedPacketUrl).then((data) => {//从配置文件中读取url
      // axios.get(config.validRedPacketUrl).then(function (response) {//从配置文件中读取url，GET请求
      validInfoList = [];
      discount=0;
      var dataList = data;
      console.log(data);
      if(data.length == 0){
        this.setState({
          haveRedpacket:false,
        });
        return;
      }
      for (var i = 0; i < dataList.length; i++) {
        validInfoList.push(dataList[i]);
      }
      console.log("validInfoList", validInfoList);
      for (var i = 0; i < validInfoList.length; i++) {
        var RedPacket = {label:'',value:''};
        var RedPacketLable = validInfoList[i].sum + "元(" + validInfoList[i].validateDate.trim().substring(0, 10) + "到期)";
        var RedPacketValue = validInfoList[i].id;
        console.log(RedPacketLable);
        RedPacket.label = RedPacketLable;
        RedPacket.value = RedPacketValue;
        RedPackets.push(RedPacket);
      }
    });
  }

  onChange = (e) => {
    var selectedIndex = e.nativeEvent.selectedSegmentIndex;
    this.setState({
      selectedIndex: selectedIndex
    })
  }

  //金额选择后将支付的实际金额改变
  onValueChange = (value) => {
    var money = 0;
    var reMoney = parseInt(value.trim().substring(1,value.length));
    money = (reMoney * 100 - discount * 100) / 100;
    this.setState({
      reMoney: reMoney,
      money:money
    })
  }
  //使用红包的checkbox
  useHongbao(e) {
    if (e.target.checked) {
      this.state.disabled = false;
      console.log("使用红包" + this.state.disabled);
      if(this.state.haveRedpacket){
        this.setState({
          checked:true
        });
      }else{
        alert("当前账户无可用红包！");
      }

    } else {
      this.state.disabled = true;
      console.log(this.state.disabled);
      this.setState({
        checked:false,
        RedPacketId:'',
        money:this.state.reMoney
      });
    }
  }
//选择器点击确定
  onOk(e) {
    this.setState({
      RedPacketId:e,
    });
    for(var i=0;i<validInfoList.length;i++){
      console.log(validInfoList[i].id);
      if(e==validInfoList[i].id){
        console.log(validInfoList[i].sum);
        discount = validInfoList[i].sum;
      }
    }
    var actMoney = (this.state.reMoney*100-discount*100)/100;
    this.setState({
      money: actMoney,
      RedPacketId:e,
    })
  }
  // onSubmit(){
  //
  // }


  render() {
    const {getFieldProps} = this.props.form;
    const {money,selectedIndex,userInfo} = this.state;

    //选择红包金额
    const rechanrgeMoneys = ['￥50', '￥100', '￥200', '￥500'];
    let chooseRedPacket;
    if (this.state.checked) {
      chooseRedPacket = (
        <Picker
          disabled={this.state.disabled}
          data={RedPackets}
          cols={1}
          onOk={(e) => this.onOk(e)}
          {...getFieldProps('district3')}
          className="forss">
          <List.Item className="recharge-list-item" arrow="horizontal">选择红包</List.Item>
        </Picker>
      )
    } else {
      chooseRedPacket = ""
    }
    // const RedPacket = [
    //   {
    //     label: '49.7元（2017-7-20到期）',
    //     value: 49.7,
    //   },
    //   {
    //     label: '86.7元（2017-7-20到期）',
    //     value: 86.7,
    //   }
    // ];
    return (
      <form>
        <WingBlank className="recharge-wingBlank">
          {/*<div >用户名：</div>*/}
          <div className="recharge-userName">用户名：{userInfo.username}</div>
          {/*<List>*/}
            {/*<Item className="recharge-list-item" disabled="true">ptyh</Item>*/}
          {/*</List>*/}
          <div className="recharge-userName" >充值金额：</div>
          <SegmentedControl className="recharge-segment"
                            selectedIndex={selectedIndex}
                            values={rechanrgeMoneys}
                            onChange={this.onChange}
                            onValueChange={this.onValueChange}
          />
          <AgreeItem className="recharge-checkbox-agree"
                     data-seed="logId"
                     onChange={(e) => this.useHongbao(e)}>
            使用红包
          </AgreeItem>
          {/*<Picker*/}
            {/*disabled={this.state.disabled}*/}
            {/*data={RedPackets}*/}
            {/*cols={1}*/}
            {/*onOk={(e) => this.onOk(e)}*/}
            {/*{...getFieldProps('district3')}*/}
            {/*className="forss">*/}
            {/*<List.Item className="recharge-list-item" arrow="horizontal">选择红包</List.Item>*/}
          {/*</Picker>*/}
          {chooseRedPacket}
          <div className="recharge-wingBlank-money">实际金额：{money}元</div>
        </WingBlank>
        <div className="recharge-btn-container">
          <Link to={{pathname:"RechargeTwo",query:{reMoney:this.state.reMoney,RedPacketId:this.state.RedPacketId}}}>
            <WingBlank>
                <Button
                  className="recharge-btn-next" type="primary" onClick={this.onSubmit} inline
                >下一步</Button>
            </WingBlank>
          </Link>
        </div>
      </form>
    );
  }
}
const RechargePart2Wrapper = createForm()(RechargePart2);
export default RechargePart2Wrapper;
