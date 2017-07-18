import React from 'react';
import { List, Badge, Flex, WhiteSpace, Icon } from 'antd-mobile';
import { Link } from 'react-router';

import './PersonCenter.less';

// 个人中心第二部分--菜单
class PersonCenter2 extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      url1: "index/personinfo",
      url2: "index/Bound",
      url3: "index/UserPay",
      url4: "index/personnotify",
    };
  }
  componentWillMount () {
    //缓存中无用户登录信息则需先登录
    if(sessionStorage.loginInfo == undefined) {
      //跳转登录界面
      var login = "login";
      this.setState({
        url1: login,
        url2: login,
        url3: login,
        url4: login,
      })
    }else {
      var url2 =  "index/Bound";
      //从缓存中读取用户个人信息
      if(sessionStorage.userInfo != undefined){
        var userInfo = JSON.parse(sessionStorage.userInfo);
        var cardId = userInfo.cardid;

        if( cardId == null|| cardId == "" || cardId == undefined){
          url2 = "index/unbound";
        }
      }
      this.setState({
        url2 : url2
      })
    }
  }
  render() {
    const { url1,url2,url3,url4} = this.state;
    return (
      <div>
        <WhiteSpace size="sm" />
        <div className="personCenter_par2_div">
          <Link to={url1}>
            <List.Item extra="" arrow="horizontal" className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge">
                <Icon
                  type={require('../../../assets/user/user-info.svg')}
                  className="personCenter_par2_icon" />
                个人信息 </Badge>
            </List.Item>
          </Link>
          <Link to={url2}>
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge">
                <Icon
                  type={require('../../../assets/user/user-card.svg')}
                  className="personCenter_par2_icon"/>
                汇智卡</Badge>
            </List.Item>
          </Link>
          <Link to={url3}>
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge">
                <Icon
                  type={require('../../../assets/user/user-pay.svg')}
                  className="personCenter_par2_icon"/>
                支付记录</Badge>
            </List.Item>
          </Link>
          <Link to={url4}>
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge">
                <Icon
                  type={require('../../../assets/user/user-notice.svg')}
                  className="personCenter_par2_icon"/>
                通知管理</Badge>
            </List.Item>
          </Link>
          <Link  >
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge">
                <Icon
                  type={require('../../../assets/user/user-set.svg')}
                  className="personCenter_par2_icon"/>
                注销</Badge>
            </List.Item>
          </Link>
        </div>
      </div>
    );
  }
}
export default PersonCenter2;
