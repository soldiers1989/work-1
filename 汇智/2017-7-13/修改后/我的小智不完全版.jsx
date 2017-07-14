import React from 'react';
import { Drawer } from 'antd-mobile';
import { Link } from 'react-router';
import axios from 'axios';
import config from '../../config';
import MyXiaozhiPart1 from '../../components/main/MyXiaozhi/MyXiaozhiPart1';
import MyXiaozhiPart2 from '../../components/main/MyXiaozhi/MyXiaozhiPart2';
import '../../components/main/MyXiaozhi/MayXiaozhi.less'
import '../../components/main/Index/index.less'
import PersonCenter1 from '../../components/user/PersonCenter/PersonCenter1';
import PersonCenterUnLogin from '../../components/user/PersonCenter/PersonCenter_UnLogin';
import PersonCenter2 from '../../components/user/PersonCenter/PersonCenter2';

var startX = 0;
var startY = 0;
var endX = 0;
var endY = 0;
var infoList = [];
var cardId = '';
var count = 0;
// 我的小智面板
class MyXiaozhi extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
      open: false,//侧边栏的默认关闭状态
      display:'none',//个人中心已登录页
      display2:'',//个人中心未登录页
      drawerdisplay:'none',//侧边栏收起时不显示
      infoList: [],//异步请求获取我的服务信息
      cardId:'',//卡号
    };
  }

  componentWillMount () {
    //从缓存中读取
    var userInfo = sessionStorage.userInfo;
    //json转换为Object对象
    var  reData = JSON.parse(userInfo);
    var cardId = reData.cardid;
    cardId = cardId.substring(4,8)+"***"+cardId.substring(10,14);
    var params ="cardNo=00199000008989&memberNo=814987924531250124";
    axios.get(config.myServiceUrl).then(function(response){//从配置文件中读取url，GET请求
      console.log("response",response);
      var dataList = response.data.result;
      console.log("infoList",infoList);
      for (var i=0; i<dataList.length; i++){
        infoList.push(dataList[i]);
      }
    });
    console.log(cardId);
  }

  componentDidMount(){
    console.log("componentDidMount",infoList);
    this.setState({
      cardId:cardId,
      infoList: infoList,
    });
  }
  componentDidUpdate(){
    console.log("componentDidUpdate",this.state.infoList);
    console.log("cardId",this.state.cardId);
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
    // if( startX < 20 && direction > 150 ){   //右滑 -->显示左侧边栏
    if( direction > 150 ){   //右滑 -->显示左侧边栏
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

  render() {
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
      <div onTouchStart={this.handleTouchStart} onTouchMove={this.handleTouchMove} onTouchEnd={this.handleTouchEnd}>
          <img
            className="MyXiaozhi_img"
            src={require('../../assets/mine/mine-title.jpg')}/>
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
        <MyXiaozhiPart1/>
        {/*<MyXiaozhiPart2/>*/}
        {/*{*/}
          {/*this.state.infoList.map((data) => {*/}
              {/*count = count + 1;*/}
              {/*return (*/}
                <MyXiaozhiPart2 {...this.state.infoList}/>
              {/*);*/}
            {/*}*/}
          {/*)*/}
        {/*}*/}
      </div>
    );
  }
}
export default MyXiaozhi;
