import React from 'react';
import { List, Switch, WingBlank, Card, Flex, Tag, Icon, Tabs } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';
import axios from 'axios';
import config from '../../../config';
import './RedPacket.less';

var validInfoList = [];
var invalidInfoList = [];
var count = 0;
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
    axios.get(config.validRedPacketUrl).then(function(response){//从配置文件中读取url，GET请求
      console.log(config.validRedPacketUrl);
      console.log("validRedPacketUrl response",response);
      validInfoList = [];
      var dataList = response.data;
      for (var i=0; i<dataList.length; i++){
        validInfoList.push(dataList[i]);
      }
      console.log("validInfoList",validInfoList);
      // this.setState({
      //   validInfoList: validInfoList,
      //   invalidInfoList: invalidInfoList
      // })
    });
    axios.get(config.invalidRedPAcketUrl).then(function(response){//从配置文件中读取url，GET请求
      console.log(config.invalidRedPAcketUrl);
      console.log("invalidRedPacketUrl response",response);
      invalidInfoList = [];
      var dataList = response.data;
      for (var i=0; i<dataList.length; i++){
        invalidInfoList.push(dataList[i]);
      }
      console.log("invalidInfoList",invalidInfoList);
    });
  }
  componentDidMount(){
    console.log("validInfoList componentDidMount",validInfoList);
    console.log("invalidInfoList componentDidMount",invalidInfoList);
    this.setState({
      validInfoList: validInfoList,
      invalidInfoList: invalidInfoList
    })
  }
  componentDidUpdate(){
    console.log("componentDidUpdate",this.state.validInfoList);
    console.log("componentDidUpdate",this.state.invalidInfoList);
  }
  render() {
    const { getFieldProps } = this.props.form;
    const {validInfoList,invalidInfoList} = this.state;
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
                        {
                          validInfoList.map((data) => {
                              count = count + 1;
                              return (
                                <div className="packet" key={count}>
                                  <div className="packet_money">
                                    <span className="yuan">¥</span><span className="big">{data.sum}</span><span className="yuan">元</span>
                                  </div>
                                  <div className="packet_time">{data.createTime}
                                  </div>
                                </div>
                              );
                            }
                          )
                        }
                      </List>
                    </div>
                  </TabPane>
                  <TabPane tab="已用/过期红包" key="2">
                    <div className="RedPacket_right">
                      <List
                        className="RedPacket_list"
                      >
                        {
                          invalidInfoList.map((data) => {
                              count = count + 1;
                              return (
                                <div className="packet" key={count}>
                                  <div className="packet_money">
                                    <span className="yuan">¥</span><span className="big">{data.sum}</span><span className="yuan">元</span>
                                  </div>
                                  <div className="packet_time">{data.createTime}
                                  </div>
                                </div>
                              );
                            }
                          )
                        }
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
