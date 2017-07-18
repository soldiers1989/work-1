import React from 'react';
import { List, Badge, Flex, WhiteSpace, Icon } from 'antd-mobile';
import { Link } from 'react-router';

import PersonCenter_1 from "../../components/user/PersonCenter/PersonCenter1";
import PersonCenter_2 from "../../components/user/PersonCenter/PersonCenter2";
// 个人中心
class PersonCenter extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      userInfo: [],
    };
  }

  componentWillMount () {
    //从缓存中读取用户个人信息
    var userInfo = JSON.parse(sessionStorage.userInfo);
    this.setState({
      userInfo: userInfo
    })
  }

  render() {
    return (
      <div>
        <PersonCenter_1 {...this.state.userInfo}/>
        <PersonCenter_2 />
      </div>
    );
  }
}
export default PersonCenter;
