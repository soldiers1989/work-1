import React from 'react';
import {Card, WingBlank, List, Icon, Button} from 'antd-mobile';
import {Link} from 'react-router';

import '../../components/main/ActiveCenter/ActiveCenter.less'
import ActiveCenter_1 from "../../components/main/ActiveCenter/ActiveCenter1";
import requestGET from "../../utils/requestGET";
import config from  "../../config";

var count = 0;
var startX = 0;
var startY = 0;
var endX = 0;
var endY = 0;
// 工作详情
class ActiveCenter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      listData: [],//当前渲染的页面数据
      // totalData:listData,
      current: 1, //当前页码
      pageSize: 4, //每页显示的条数
      totalPage: 0,//总页数
    };
  }

  handleTouchStart=(e)=> {
    startX = e.changedTouches[0].pageX;
    startY = e.changedTouches[0].pageY;
    //console.log("startX======" + startX);
    //console.log("startY======" + startY);
  }

  handleTouchMove=(e)=> {
    //console.log(e.changedTouches[0].pageX);
  }

  handleTouchEnd=(e)=> {
    endX = e.changedTouches[0].pageX;
    endY = e.changedTouches[0].pageY;
    //console.log("endX======" + endX);
    //console.log("endY======" + endY);
    console.log("sx======" + startX);
    console.log("sy======" + startY);
    console.log("ex======" + endX);
    console.log("ey======" + endY);
    var direction = endX - startX;
    console.log(direction);
    if(direction >300){   //右滑 -->上一页
      if (this.state.current > 1) {
        var prePage = this.state.current - 1;
        var url = config.actCenterUrl.replace("page", prePage).replace("pageSize", this.state.pageSize);
        requestGET(url).then((data) => {//从配置文件中读取url
          var listData = data.msg.result;
          var current = data.msg.page;
          this.setState({
            listData: listData,
            current: prePage
          })
        })
      }
    }
    if(direction < -200){   ////左滑 -->下一页
      if (this.state.current < this.state.totalPage) {
        var nextPage = this.state.current + 1;
        var url = config.actCenterUrl.replace("page", nextPage).replace("pageSize", this.state.pageSize);
        requestGET(url).then((data) => {//从配置文件中读取url
          var listData = data.msg.result;
          var current = data.msg.page;
          this.setState({
            listData: listData,
            current: nextPage
          })
        })
      }
    }
  }
  componentWillMount() {
    var url = config.actCenterUrl.replace("page", this.state.current).replace("pageSize", this.state.pageSize);
    requestGET(url).then((data) => {//从配置文件中读取url
      var listData = data.msg.result;
      var totalPage = Math.ceil(data.msg.total / this.state.pageSize);
      var current = data.msg.page;
      this.setState({
        listData: listData,
        totalPage: totalPage,
      })
    });
  }

  render() {
    const {listData} = this.state;

    return (
      <div onTouchStart={this.handleTouchStart} onTouchMove={this.handleTouchMove} onTouchEnd={this.handleTouchEnd}>
        <img className="activeCenter_img"
             src={require('../../assets/active/active-title.jpg')}/>
        {
          listData.map((data) => {
              count = count + 1;
              return (
                <ActiveCenter_1 {...data} key={count}/>
              );
            }
          )
        }
        <span>{ this.state.current }页/ { this.state.totalPage }页</span>
      </div>
    );
  }
}
export default ActiveCenter;
