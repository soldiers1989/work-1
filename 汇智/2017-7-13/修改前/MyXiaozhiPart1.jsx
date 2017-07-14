import React from 'react';
import { List, WingBlank, Card, Flex } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';
import requestGET from '../../../utils/requestGET';
import request from '../../../utils/request';
import config from '../../../config';
import './MayXiaozhi.less'
// 我的小智面板
class MyXiaozhiPart1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      infoList: [],
      cardId:[]
    };
  }
  componentWillMount () {
    //从缓存中读取
    var userInfo = sessionStorage.userInfo;
    //json转换为Object对象
    var  reData = JSON.parse(userInfo);
    var cardid = reData.cardid;
    cardid = cardid.substring(4,8)+"***"+cardid.substring(10,14);
    this.setState({
      cardId : cardid
    });
    var params ="cardNo=00199000008989&memberNo=814987924531250124";
    // request(config.cardBalanceUrl,params).then((data) => {//从配置文件中读取url
    //   var infoList = data;
    //   this.setState({
    //     infoList : infoList
    //   })
    //   console.log(infoList);
    // })
  }
  render() {
    const { getFieldProps } = this.props.form;
    const {cardId} = this.state;
    return (
        <List className="MyXiaozhi_list">
          <Card>
            <div className="MyXiaozhi_cardNo" >汇智卡号：{cardId}</div>

              <Flex>
                <Flex.Item>
                  <div className="MyXiaozhi_money" >电子钱包余额</div>
                  <div className="MyXiaozhi_money_bg" >
                    <div className="MyXiaozhi_money_hidden" >***</div>
                  </div>
                </Flex.Item>
                <Flex.Item  className="MyXiaozhi_Flex">
                  <div className="MyXiaozhi_money">主账户余额</div>
                  <div className="MyXiaozhi_money_bg" >
                    <div className="MyXiaozhi_money_hidden">***</div>
                  </div>
                </Flex.Item>
              </Flex>

            <Card.Footer className="MyXiaozhi_money" content={'（以卡内实际金额为准，单位：元）'} />
          </Card>
        </List>
    );
  }
}
const MyXiaozhiPart1Wrapper = createForm()(MyXiaozhiPart1);
export default MyXiaozhiPart1Wrapper;
