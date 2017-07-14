import React from 'react';
import { List, Switch, WingBlank, Card, Flex, Tag, Icon, Tabs } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';
import requestGET from '../../../utils/requestGET';
import request from '../../../utils/request';
import config from '../../../config';
import './RedPacket.less';


const TabPane = Tabs.TabPane;

// 红包
class part1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      validInfoList: [],
      invalidInfoList: []
    };
  }
  componentWillMount () {
    requestGET(config.validRedPacketUrl).then((data) => {//从配置文件中读取url
      var validInfoList = data;
      this.setState({
        infoList : validInfoList
      })
      console.log(validInfoList);
    });

    requestGET(config.invalidRedPAcketUrl).then((data) => {//从配置文件中读取url
      var invalidInfoList = data;
      this.setState({
        infoList : invalidInfoList
      })
      console.log(invalidInfoList);
    });
  }
  render() {
    const { getFieldProps } = this.props.form;
    return (
      <form>
        {/*<List>*/}
          {/*<img className="RedPacket_img" src={require('../../../assets//mine/mine-p1.jpg')} alt="图片" />*/}
        {/*</List>*/}
        <WingBlank>
          <List>
            <Card>
              <Card.Body>
                <Tabs>
                  <TabPane tab="可用红包" key="1">
                    <div className="RedPacket_left">
                      <List
                        className="RedPacket_list"
                      >
                        <div className="packet">
                          <div className="packet_money">
                            <span className="yuan">¥</span><span className="big">50.0</span><span className="yuan">元</span>
                          </div>
                          <div className="packet_time">2016.11.22-2017.11.23
                          </div>
                        </div>
                        <div className="packet">
                          <div className="packet_money">
                            <span className="yuan">¥</span><span className="big">50.0</span><span className="yuan">元</span>
                          </div>
                          <div className="packet_time">2016.11.22-2017.11.23
                          </div>
                        </div>
                        <div className="packet">
                          <div className="packet_money">
                            <span className="yuan">¥</span><span className="big">50.0</span><span className="yuan">元</span>
                          </div>
                          <div className="packet_time">2016.11.22-2017.11.23
                          </div>
                        </div>
                      </List>
                    </div>
                  </TabPane>
                  <TabPane tab="已用/过期红包" key="2">
                    <div className="RedPacket_right">
                      <List
                        className="RedPacket_list"
                      >
                        <div className="packet_no packet">
                          <div className="packet_money">
                            <span className="yuan">¥</span><span className="big">50.0</span><span className="yuan">元</span>
                          </div>
                          <div className="packet_time">2016.11.22-2017.11.23
                          </div>
                        </div>
                        <div className="packet packet_no">
                          <div className="packet_money">
                            <span className="yuan">¥</span><span className="big">50.0</span><span className="yuan">元</span>
                          </div>
                          <div className="packet_time">2016.11.22-2017.11.23
                          </div>
                        </div>
                        <div className="packet packet_no">
                          <div className="packet_money">
                            <span className="yuan">¥</span><span className="big">50.0</span><span className="yuan">元</span>
                          </div>
                          <div className="packet_time">2016.11.22-2017.11.23
                          </div>
                        </div>
                      </List>
                    </div>
                  </TabPane>
                </Tabs>
              </Card.Body>
            </Card>
          </List>
        </WingBlank>
      </form>
    );
  }
}
const part1Wrapper = createForm()(part1);
export default part1Wrapper;
