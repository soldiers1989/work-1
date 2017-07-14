import React from 'react';
import { List, Badge, Flex, WhiteSpace, Icon } from 'antd-mobile';
import { Link } from 'react-router';

import './PersonCenter.less';

// 个人中心第二部分--菜单
class PersonCenter2 extends React.Component {
  render() {
    return (
      <div>
        <WhiteSpace size="sm" />
        <div className="personCenter_par2_div">
          <Link to="index/personinfo">
            <List.Item extra="" arrow="horizontal" className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge"><Icon type={require('../../../assets/user/user-info.svg')} className="tabSelect-icon" />      个人信息 </Badge>
            </List.Item>
          </Link>
          <Link to="index/Unbound">
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge"><Icon type={require('../../../assets/user/user-card.svg')} className="tabSelect-icon" />      汇智卡</Badge>
            </List.Item>
          </Link>
          <Link to="index/UserPay">
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge"><Icon type={require('../../../assets/user/user-pay.svg')} className="tabSelect-icon" />     支付记录</Badge>
            </List.Item>
          </Link>
          <Link to="index/personnotify">
            <List.Item extra="" arrow="horizontal"  className="personCenter_par2_List">
              {/* TODO-ICON */}
              <Badge text={0} className="personCenter_par2_Badge"><Icon type={require('../../../assets/user/user-notice.svg')} className="tabSelect-icon" />     通知管理</Badge>
            </List.Item>
          </Link>
        </div>
      </div>
    );
  }
}
export default PersonCenter2;
