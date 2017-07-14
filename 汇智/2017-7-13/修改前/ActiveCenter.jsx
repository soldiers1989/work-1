import React from 'react';
import {Card, WingBlank, List, Drawer, Icon, Button} from 'antd-mobile';
import {Link} from 'react-router';

import '../../components/main/ActiveCenter/ActiveCenter.less'
import ActiveCenter_1 from "../../components/main/ActiveCenter/ActiveCenter1";
import requestGET from "../../utils/requestGET";
import config from  "../../config";
import '../../components/main/Index/index.less'
import PersonCenter1 from '../../components/user/PersonCenter/PersonCenter1';
import PersonCenterUnLogin from '../../components/user/PersonCenter/PersonCenter_UnLogin';
import PersonCenter2 from '../../components/user/PersonCenter/PersonCenter2';

var count = 0;
var startX = 0;
var startY = 0;
var endX = 0;
var endY = 0;
var bodyHeight = 0;
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
      open: false,//侧边栏的默认关闭状态
      display:'none',//个人中心已登录页
      display2:'',//个人中心未登录页
      drawerdisplay:'none',//侧边栏收起时不显示
    };
  }

  handleTouchStart = (e) => {
    startX = e.changedTouches[0].pageX;
    startY = e.changedTouches[0].pageY;
    //console.log("startX======" + startX);
    //console.log("startY======" + startY);
  }

  handleTouchMove = (e) => {
    //console.log(e.changedTouches[0].pageX);
  }

  handleTouchEnd = (e) => {
    endX = e.changedTouches[0].pageX;
    endY = e.changedTouches[0].pageY;
    console.log("sx======" + startX);
    console.log("sy======" + startY);
    console.log("ex======" + endX);
    console.log("ey======" + endY);
    var direction = endX - startX;   //横向
    //console.log(direction);
    var portrait = endY - startY;   //纵向
    //console.log(portrait);
    if(Math.abs(direction)>Math.abs(portrait) && direction > 0 && startX > 20){   //右滑-->上一页
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
    if(Math.abs(direction)>Math.abs(portrait) && direction < 0 && !this.state.open){   ////左滑 -->下一页
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
    if( startX < 20 && direction > 150 ){   //右滑 -->显示左侧边栏
    // if( direction > 150 ){   //右滑 -->显示左侧边栏
      this.checkLogon();
      this.setState({
        drawerdisplay:'',
      });
      //设置打开的后延
      setTimeout(() => {
        this.setState({
          open: true,
        });
      }, 50);
    }
    if( this.state.open && direction < -150 ){   ////左滑 -->收起左侧边栏
      this.setState({
        open:false,
      });
      setTimeout(() => {
        this.setState({
          drawerdisplay:'none',
        });
      }, 50);
    }
  }
//判断是否登录，并设置个人中心的页面显示与不显示
  checkLogon=()=>{
    if(sessionStorage.userInfo){
      console.log("已登录！");
      this.setState({
        display:'',
        display2:'none',
      });
    }else {
      console.log("未登录，请登录！");
      this.setState({
        display:'none',
        display2:'',
      });
    }
  }
  //侧边栏的开关事件
  onOpenChange = (...args) => {
    this.checkLogon();
    console.log(args);
    this.setState({
      open: !this.state.open
    });
    setTimeout(() => {
      this.setState({
        drawerdisplay:'none',
      });
    }, 50);
  }

  componentWillMount() {
    /*var windowHeight = window.screen.height;
    console.log(windowHeight);
    bodyHeight= document.body.clientHeight;*/
    console.log(bodyHeight);
    var url = config.actCenterUrl.replace("page", this.state.current).replace("pageSize", this.state.pageSize);
    var lastUrl = this.props.location.pathname;
    sessionStorage.lastUrl = lastUrl;
    console.log(lastUrl);
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
    //侧边栏包含的内容
    const sidebar = (<div className="div-siderbar">
      <div style={{display:this.state.display}}>
        <PersonCenter1/>
      </div>
      <div style={{display:this.state.display2}}>
        <PersonCenterUnLogin/>
      </div>
      <PersonCenter2/>
    </div>);
    return (
      <div onTouchStart={this.handleTouchStart} onTouchMove={this.handleTouchMove} onTouchEnd={this.handleTouchEnd} className="activeCenter_top">
        <img className="activeCenter_img"
             src={require('../../assets/active/active-title.jpg')}/>
        <Drawer
          className="my-drawer"
          style={{ minHeight: document.documentElement.clientHeight - 200,display:this.state.drawerdisplay}}
          enableDragHandle
          contentStyle={{ color: '#A6A6A6', textAlign: 'center', paddingTop: 42 }}
          sidebar={sidebar}
          position={'left'}
          open={this.state.open}
          onOpenChange={this.onOpenChange}
        />
        {
          listData.map((data) => {
              count = count + 1;
              return (
                <ActiveCenter_1 {...data} key={count}/>
              );
            }
          )
        }
        <span className="activeCenter_totalPage">{ this.state.current }页/ { this.state.totalPage }页</span>
      </div>
    );
  }
}
export default ActiveCenter;
