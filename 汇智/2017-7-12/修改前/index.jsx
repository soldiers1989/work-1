/**
 * 首页第二部分--九宫格
 */
import React from 'react';
import {Link} from 'react-router';
import { Carousel, WhiteSpace, WingBlank,Icon } from 'antd-mobile';

import './index.less'

class part2 extends React.Component {

  render() {
    return (
      <div className="index_par2_div">
        <li>
          <Link className="index_link" to="Recharge">
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-recharge.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/home/03.png')} />*/}
            <p>充值</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to="index/Bound">
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-balance.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/04.png')} />*/}
            <p>余额查询</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to="ApplyCard">
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-payment.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/05.png')} />*/}
            <p>付款码</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to="index/propertyRepair">
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-repair.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/07.png')} />*/}
            <p>物业报修</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to="index/itRepair">
            {/*// TODO-ICON*/}
            <Icon type={require('../../../assets/home/home-itrepair.svg')} className="index_Icon"/>
            {/*<img src={require('../../assets/08.png')} />*/}
            <p>IT报修</p>
          </Link>
        </li>
        <li>
          <Link className="index_link" to="index/instruction">
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
