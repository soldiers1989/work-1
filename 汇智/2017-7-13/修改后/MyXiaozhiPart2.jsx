import React from 'react';
import { List, WingBlank, Card, Tag, Icon, Tabs } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';
import requestGET from '../../../utils/requestGET';
import axios from 'axios';
import config from '../../../config';
import './MayXiaozhi.less'

const TabPane = Tabs.TabPane;
const Item = List.Item;
var infoList = [];
var count = 0;
// const ServiceType = [{ label: '物业报修', value: '1' }, { label: 'IT报修', value: '2' }];
// 我的小智面板
class MyXiaozhiPart2 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      infoList: []
    };
  }

  componentWillMount () {
    axios.get(config.myServiceUrl).then(function(response){//从配置文件中读取url，GET请求
      console.log("response",response);
      var dataList = response.data.result;
      console.log("infoList",infoList);
      for (var i=0; i<dataList.length; i++){
        infoList.push(dataList[i]);
      }
    });
  }

  componentDidMount(){
    console.log("componentDidMount",infoList);
    this.setState({
      infoList: infoList
    })
  }
  componentDidUpdate(){
    console.log("componentDidUpdate",this.state.infoList);
  }

  render() {
    const { getFieldProps } = this.props.form;
    const {infolist} = this.state;
    return (
      <div>
        <List className="MyXiaozhi_list">
          <Card className="card_none">
            <Card.Body>
              <Tabs>
                <TabPane tab="我的服务" key="1">
                  <div >
                    <List className="MyXiaozhi_fontSize">
                      {
                        infoList.map((data) => {
                            count = count + 1;
                            return (
                              <Item wrap arrow="horizontal" multipleLine className="MyXiaozhi_Item" key={count}>
                                <div>流水号：{data.sn}</div>
                                <div>时间：{data.addtime}</div>
                                <div>类别：{data.type}</div>
                                <div>状态：{data.current_state}</div>
                              </Item>
                            );
                          }
                        )
                      }
                      <Item wrap arrow="horizontal" multipleLine className="MyXiaozhi_Item">
                        <div>流水号：123456798</div>
                        <div>时间：2017-06-27 14:02</div>
                        <div>类别：物业维修</div>
                        <div>状态：待处理</div>
                      </Item>
                    </List>
                    <Icon type="left" />
                    <Icon type="right" />
                  </div>
                </TabPane>
                <TabPane tab="我的申请" key="2">
                  <div >
                    <List className="MyXiaozhi_fontSize">
                      <Item wrap arrow="horizontal" multipleLine className="MyXiaozhi_Item">
                        <div>流水号：123456798</div>
                        <div>预约时间：2017-07-15 14:00-15:00</div>
                        <div>类别：游船预约</div>
                        <div>状态：待处理</div>
                      </Item>
                      <Item wrap arrow="horizontal" multipleLine className="MyXiaozhi_Item">
                        <div>流水号：123456798</div>
                        <div>预约时间：2017-07-15 14:00-15:00</div>
                        <div>类别：游船预约</div>
                        <div>状态：待处理</div>
                      </Item>
                    </List>
                    <Icon type="left" />
                    <Icon type="right" />
                  </div>
                </TabPane>
              </Tabs>
            </Card.Body>
          </Card>
        </List>
      </div>
    );
  }
}
const MyXiaozhiPart2Wrapper = createForm()(MyXiaozhiPart2);
export default MyXiaozhiPart2Wrapper;
