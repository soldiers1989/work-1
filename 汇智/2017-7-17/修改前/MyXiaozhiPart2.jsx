import React from 'react';
import { List, WingBlank, Card, Tag, Icon, Tabs } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';
import axios from 'axios';
import config from '../../../config';
import './MayXiaozhi.less'

const TabPane = Tabs.TabPane;
const Item = List.Item;
var infoList = [];
var infoOrderList = [];
var count = 0;
// 我的小智面板
class MyXiaozhiPart2 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      infoList: [],
      infoOrderList:[],
    };
  }

  componentWillMount () {
    axios.get(config.myServiceUrl).then(function(response){//从配置文件中读取url，GET请求
      console.log("response",response);
      infoList = [];
      var dataList = response.data.result;
      console.log("infoList",infoList);
      for (var i=0; i<dataList.length; i++){
        infoList.push(dataList[i]);
      }
    });
    axios.get(config.myOrderUrl).then(function(response){//从配置文件中读取url，GET请求
      console.log("response",response);
      infoOrderList = [];
      var dataList = response.data.result;
      console.log("infoOrderList",infoOrderList);
      for (var i=0; i<dataList.length; i++){
        infoOrderList.push(dataList[i]);
      }
    });
  }

  componentDidMount(){
    console.log("componentDidMount",infoList);
    this.setState({
      infoList: infoList,
      infoOrderList:infoOrderList,
    })
  }
  componentDidUpdate(){
    console.log("componentDidUpdate",this.state.infoList);
  }

  render() {
    const { getFieldProps } = this.props.form;
    const {infolist,infoOrderList} = this.state;
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
                            {/*const detail = [*/}
                              {/*{*/}
                                {/*label: '流水号',*/}
                                {/*value: data.sn,*/}
                              {/*},*/}
                              {/*{*/}
                                {/*label: '时间',*/}
                                {/*value: data.addtime,*/}
                              {/*},*/}
                              {/*{*/}
                                {/*label: '类别',*/}
                                {/*value: data.process_name,*/}
                              {/*},*/}
                              {/*{*/}
                                {/*label: '状态',*/}
                                {/*value: data.current_state,*/}
                              {/*}*/}
                            {/*];*/}
                            return (
                              <Link
                                to={{
                                  pathname:"Detail",
                                  query:{sn:data.sn,
                                          addtime:data.addtime,
                                          process_name:data.process_name,
                                          current_state:data.current_state}}}
                                key={count}>
                                <Item wrap arrow="horizontal" multipleLine className="MyXiaozhi_Item">
                                  <div>流水号：{data.sn}</div>
                                  <div>时间：{data.addtime}</div>
                                  <div>类别：{data.process_name}</div>
                                  <div>状态：{data.current_state}</div>
                                </Item>
                              </Link>
                            );
                          }
                        )
                      }
                    </List>
                    <Icon type="left" />
                    <Icon type="right" />
                  </div>
                </TabPane>
                <TabPane tab="我的申请" key="2">
                  <div >
                    <List className="MyXiaozhi_fontSize">
                      {
                        infoOrderList.map((data) => {
                            count = count + 1;
                            return (
                              <Link
                                to={{
                                  pathname:"Detail_apply",
                                  query:{objectid:data.objectid,
                                    dingStartTime:data.dingStartTime,
                                    company:data.company,
                                    createTime:data.createTime}}}
                                key={count}>
                              <Item wrap arrow="horizontal" multipleLine className="MyXiaozhi_Item">
                                <div>流水号：{data.objectid}</div>
                                <div>时间：{data.dingStartTime}</div>
                                <div>类别：{data.company}</div>
                                <div>状态：{data.createTime}</div>
                              </Item>
                              </Link>
                            );
                          }
                        )
                      }
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
