import React from 'react';
import { List, Badge, Icon } from 'antd-mobile';
import { Link } from 'react-router';

import './PersonInfo.less';

// 个人信息页面
class PersonInfo1 extends React.Component {
  render() {
    return (
        <div className="personInfo_par1_div">
          <Link to="index/userinfo">
            <List.Item extra="" arrow="horizontal" style={{ borderBottom: '1px solid #ccc' }}>
              {/* TODO-ICON */}
              <Badge text={0} style={{ marginLeft: 12 }}><Icon type={require('../../../assets/user/user-info.svg')} className="tabSelect-icon" /></Badge><span className="personInfo_par1_span">个人资料</span>
            </List.Item>
          </Link>
          <Link to="index/activedetail">
            <List.Item extra="" arrow="horizontal" style={{ borderBottom: '1px solid #ccc' }}>
              {/* TODO-ICON */}
              <Badge text={0} style={{ marginLeft: 12 }}><Icon type={require('../../../assets/user/user-set.svg')} className="tabSelect-icon" /></Badge><span className="personInfo_par1_span">头像设置</span>
            </List.Item>
          </Link>
          <Link to="ChangePwd">
            <List.Item extra="" arrow="horizontal" style={{ borderBottom: '1px solid #ccc' }}>
              {/* TODO-ICON */}
              <Badge text={0} style={{ marginLeft: 12 }}><Icon type={require('../../../assets/user/user-pwd.svg')} className="tabSelect-icon" /></Badge><span className="personInfo_par1_span">修改密码</span>
            </List.Item>
          </Link>
          <Link to="ChangePhoneNo">
            <List.Item extra="" arrow="horizontal" style={{ borderBottom: '1px solid #ccc' }}>
              {/* TODO-ICON */}
              <Badge text={0} style={{ marginLeft: 12 }}><Icon type={require('../../../assets/user/user-phone.svg')} className="tabSelect-icon" /></Badge><span className="personInfo_par1_span">修改手机号</span>
            </List.Item>
          </Link>
        </div>
    );
  }
}
export default PersonInfo1;
