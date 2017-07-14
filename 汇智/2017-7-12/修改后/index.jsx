/**
 * 首页第二部分--九宫格
 */
import React from 'react';
import {Link} from 'react-router';
import { Carousel, WhiteSpace, WingBlank,Icon } from 'antd-mobile';

import './index.less'

class part2 extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      url1: "Recharge",
      url2: "index/Bound",
      url3: "ApplyCard",
      url4: "index/propertyRepair",
      url5: "Rindex/itRepair",
      url6: "index/instruction",
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
        url5: login,
        url6: login
      })
    }else {
      var url3 =  "index/Bound";
      //从缓存中读取用户个人信息
      if(sessionStorage.userInfo != undefined){
        var userInfo = JSON.parse(sessionStorage.userInfo);
        var cardId = userInfo.cardid;

        if(cardId == "" || cardId.length == 0 || cardId == undefined){
          url3 = "index/unbound";
        }
      }
      this.setState({
        url3 : url3
      })
    }
  }
  render() {
    const { url1,url2,url3,url4,url5,url6 } = this.state;
    return (
      <div className="index_par2_div">
        <li>
          <Link className="index_link" to={url1}>
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-recharge.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/home/03.png')} />*/}
            <p>充值</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to={url2}>
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-balance.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/04.png')} />*/}
            <p>余额查询</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to={url3}>
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-payment.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/05.png')} />*/}
            <p>付款码</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to={url4}>
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-repair.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/07.png')} />*/}
            <p>物业报修</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to={url5}>
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-itrepair.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/08.png')} />*/}
            <p>IT报修</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to={url6}>
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-ship.svg')}  className="index_Icon"/>
            {/*<img src={require('../../assets/09.png')} />*/}
            <p>游船预约</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to="index/APP">
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-add.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/10.png')} />*/}
            <p>更多</p>
          </Link>
        </li>
    </div>
  );
  }
}

export default part2;
