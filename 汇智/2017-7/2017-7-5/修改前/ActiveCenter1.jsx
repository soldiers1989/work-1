import React from 'react';
import { Card, WingBlank, List, Icon } from 'antd-mobile';
import { Link } from 'react-router';

import './ActiveCenter.less';

const Item = List.Item;
const Brief = Item.Brief;

// 工作详情
class ActiveCenter1 extends React.Component {
  render() {
    return (
      <div className="activeCenter_par1_div">
        <WingBlank>
          <Link to="index/personnotify">
            <Card className="index_menu">
              <Item
                thumb={<div><img src={require('../../../assets/active/active-p1.jpg')} alt="图片" /></div>}
                onClick={() => {
                }}
              >
                <span>上海浦东软件园哈登金卡和点卡</span>
                <Brief>
                  {/* TODO-ICON */}
                  <Icon type={require('../../../assets/active/active-label1.svg')} className="tabSelect-icon" />
                  <span>2017-6-29 10:45:54</span>
                </Brief>
              </Item>
            </Card>
          </Link>
          <Link to="index/personnotify">
            <Card className="index_menu">
              <Item
                thumb={
                  <div><img className="" src={require('../../../assets/active/active-p2.jpg')} alt="图片" />
                  </div>
                }
                onClick={() => {
                }}
              >
                <span>上海浦东软件园哈登金卡和点卡</span>
                <Brief>
                  {/* TODO-ICON */}
                  <Icon type={require('../../../assets/active/active-label1.svg')} className="tabSelect-icon" />
                  <span>2017-6-29 10:45:54</span>
                </Brief>
              </Item>
            </Card>
          </Link>
        </WingBlank>
      </div>
    );
  }
}
export default ActiveCenter1;
