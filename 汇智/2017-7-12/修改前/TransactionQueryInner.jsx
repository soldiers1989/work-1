import {Table, WingBlank, Card, List, Tabs} from 'antd-mobile';
import {createForm} from 'rc-form';
import React from 'react';
import './TransactionQueryInner.less';
import config from '../../../config';
import requestGET from '../../../utils/requestGET';
import request from '../../../utils/request';

const TabPane = Tabs.TabPane;
const Item = List.Item;

// 交易明细
class TransactionQueryInner extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      perNoList: []
    };
  }
  componentWillMount () {
    var params = "merchantNo=000006666666666"+"&cardNo=00199000008989"+"&endDate="+"&memberNo=ptyh"+"&queryNum="+"&startDate="+"&tranType=01";
    request(config.queryTransDetails,params).then((data) => {//从配置文件中读取url
      var infoList = data;
      this.setState({
        infoList : infoList
      })
      console.log(infoList);
    });
  }
  render() {
    const {getFieldProps} = this.props.form;
    const data = [
      {refNo: '001', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '1'},
      {refNo: '002', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '2'},
      {refNo: '003', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '3'},
      {refNo: '004', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '4'},
      {refNo: '005', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '5'},
      {refNo: '006', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '6'},
      {refNo: '007', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '7'},
      {refNo: '008', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '8'},
      {refNo: '009', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '9'},
      {refNo: '010', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '10'},
      {refNo: '011', transTime: '2017/04/05 14:29', transMoney: '300', transType: '圈存', shopName: '合作商户', key: '11'},
    ].map((s) => <Card>
      <List className="transaction-list">
        <Item className="transaction-list-item">
          流 水 号 : {s.refNo}
        </Item>
        <Item className="transaction-list-item">
          交易时间 : {s.transTime}
        </Item>
        <Item className="transaction-list-item">
          交易金额 : {s.transMoney}
        </Item>
        <Item className="transaction-list-item">
          交易类型 : {s.transType}
        </Item>
        <Item className="transaction-list-item">
          商户名称 : {s.shopName}
        </Item>
      </List>
    </Card>);
    return (
      <div>
        <Tabs>
          <TabPane tab="主账户记录" key="1" className="transaction-tabpane">
            <div className="transaction-wingBlank-tab">
              <WingBlank className="transaction-wingBlank">
                {data}
              </WingBlank>
            </div>
          </TabPane>
          <TabPane tab="充值记录" key="2" className="transaction-tabpane">
            <div className="transaction-table-pan">
              <WingBlank className="transaction-wingBlank">
                {data}
              </WingBlank>
            </div>
          </TabPane>
          <TabPane tab="电子钱包记录" key="3" className="transaction-tabpane">
            <div className="transaction-table-pan">
              <WingBlank className="transaction-wingBlank">
                {data}
              </WingBlank>
            </div>
          </TabPane>
          <TabPane tab="在线充值记录" key="4" className="transaction-tabpane">
            <div className="transaction-table-pan">
              <WingBlank className="transaction-wingBlank">
                {data}
              </WingBlank>
            </div>
          </TabPane>
        </Tabs>
      </div>
    );
  }
}

const TransactionQueryInnerWrapper = createForm()(TransactionQueryInner);
export default TransactionQueryInnerWrapper;
