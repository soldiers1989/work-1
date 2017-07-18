import React from 'react';
import { Link } from 'react-router';
import { List, WingBlank, Card, Flex } from 'antd-mobile';
import '../../main/MyXiaozhi/MayXiaozhi.less';
import axios from 'axios';
import Qs from 'qs';
import config from '../../../config';
var infoList = [];
class BoundBar extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      infoList: [],
      cardId:[]
    };
  }
  componentWillMount () {
    var nowDate = new Date().getTime();
    console.log(nowDate);
    //卡号和会员号要从缓存中读取
    var userInfo = localStorage.userInfo;
    //json转换为Object对象
    var  reData = JSON.parse(userInfo);
    var cardid = reData.cardid;
    cardid = cardid.substring(4,8)+"***"+cardid.substring(10,14);
    this.setState({
      cardId : cardid,
    })

    var data = {
      cardNo:"0000"+ reData.cardid ,
      memberNo: reData.username
    };
    console.log(data);
    axios.post(config.cardBalanceUrl,Qs.stringify(data)).then(function(response){//从配置文件中读取url，GET请求
      console.log("cardBalanceUrl response",response);
        infoList = [];
        var dataList = response.data;
        console.log("infoList",infoList);
        for (var i=0; i<dataList.length; i++){
          infoList.push(dataList[i]);
        }
    });
  }
  componentDidMount(){
    console.log("componentDidMount",infoList);
    this.setState({
      infoList: infoList
    })
  }
  componentDidUpdate(){
    console.log("componentDidUpdate",this.state.infoList);
  }
  render() {
    const {infoList} = this.state;
    const {cardId} = this.state;
    return (
      <div>
        <Link to="index/Index">
            <img
              className="MyXiaozhi_img"
              src={require('../../../assets/card/card-p1.jpg')}
              alt="图片" />
        </Link>

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

                <Card.Footer
                  className="MyXiaozhi_money"
                  content={'（以卡内实际金额为准，单位：元）'} />
              </Card>
            </List>

      </div>
    );
  }
}
export default BoundBar;
