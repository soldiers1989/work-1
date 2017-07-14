import React from 'react';
import { List, Switch, WingBlank, Card, Flex, Tag, Icon, Tabs } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';
import './RedPacket.less';
const TabPane = Tabs.TabPane;

// 红包
class part1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      perNoList: []
    };
  }
  componentWillMount () {
    fetch('http://localhost:3003/RedPacket')
      .then(res => res.json())
      .then(res => {
        this.setState({
          perNoList: res
        });
      });
  }
  render() {
    const { getFieldProps } = this.props.form;
    const {perNoList} = this.state;
    return (
      <form>
        <List>
          <img className="RedPacket_img" src={require('../../../assets//mine/mine-p1.jpg')} alt="图片" />
        </List>
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
                        {
                          perNoList.map((perNo) => {
                            return (
                              <div className="packet">
                                <div className="packet_money">
                                  <span className="yuan">¥</span><span className="big">{perNo.amount}</span><span className="yuan">元</span>
                                </div>
                                <div className="packet_time">{perNo.time}
                                </div>
                              </div>
                            );
                          })
                        }
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
                        {
                          perNoList.map((perNo) => {
                            return (
                              <div className="packet">
                                <div className="packet_money">
                                  <span className="yuan">¥</span><span className="big">{perNo.amount}</span><span className="yuan">元</span>
                                </div>
                                <div className="packet_time">{perNo.time}
                                </div>
                              </div>
                            );
                          })
                        }
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
