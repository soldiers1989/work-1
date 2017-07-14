import React from 'react';
import { List, WingBlank, Card, Flex } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';

import './MayXiaozhi.less'
// 我的小智面板
class MyXiaozhiPart1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
    };
  }

  render() {
    const { getFieldProps } = this.props.form;

    return (
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

            <Card.Footer className="MyXiaozhi_money" content={'（以卡内实际金额为准，单位：元）'} />
          </Card>
        </List>
    );
  }
}
const MyXiaozhiPart1Wrapper = createForm()(MyXiaozhiPart1);
export default MyXiaozhiPart1Wrapper;
