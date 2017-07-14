import React from 'react';
import { Link } from 'react-router';
import { List, WingBlank, Card, Flex } from 'antd-mobile';
import '../../main/MyXiaozhi/MayXiaozhi.less';

class BoundBar extends React.Component {
  render() {
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
                <div className="MyXiaozhi_cardNo" >汇智卡号：1234***9123</div>

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
