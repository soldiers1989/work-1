import React from 'react';
import { List, Badge, Flex, WhiteSpace, Icon } from 'antd-mobile';
import { Link } from 'react-router';

import './PersonCenter.less';
import config from '../../../config';

// 个人中心第一部分--头像
class PersonCenter1 extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      userInfo: [],
      userFace:"../../../assets/user/user-none.png",
    };
  }

  componentWillMount () {
    if(sessionStorage.userInfo != undefined){
      var userInfo = JSON.parse(sessionStorage.userInfo);
      var userFace = "../../../assets/user/user-none.png";
      if(userInfo.userFace != null){
        userFace = config.httpUrl+userInfo.userFace;
      }
      this.setState({
        userInfo: userInfo,
        userFace:userFace,
      })
    }
  }
  render() {
    // const {userFace,name} = this.props;
    const {userInfo,userFace} =this.state;
    return (
        <div className="personCenter_par1_div">
          <WhiteSpace size="lg" />
          <Flex  className="personCenter_par1_Flex">
            <Flex.Item>个人中心</Flex.Item>
          </Flex>
          <WhiteSpace size="lg" />
          <Flex className="personCenter_par1_Flex">
            <Flex.Item>
              <img
                src={userFace}
                // src={require('../../../assets/user/user-none.png')}
                className="personCenter_par1_png" alt="图片" />
            </Flex.Item>
          </Flex>
          <WhiteSpace size="lg" />

            <Flex className="personCenter_par1_Flex">
              <Flex.Item>{userInfo.name}</Flex.Item>
            </Flex>
          <WhiteSpace size="xl" />
        </div>
    );
  }
}
export default PersonCenter1;
