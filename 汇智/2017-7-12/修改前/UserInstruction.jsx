import React from 'react';
import { List, Button ,Modal} from 'antd-mobile';
import { Link } from 'react-router';

import './instruction.less';

const Item = List.Item;
const prompt = Modal.prompt;

class UserInstruction extends React.Component {
  state={
    display1: 'none',
    display2: '',
  }
  componentWillMount(){
    if(!sessionStorage.userInfo){
      console.log("未登录，请登录！");
    }else {
      //从缓存中读取
      var userInfo = sessionStorage.userInfo;
      //json转换为Object对象
      var reData = JSON.parse(userInfo);

      if (reData.cardid) {
        console.log("有cardID");
        this.state.display1='';
        this.state.display2='none';
      } else {
        this.state.display1='none'
        this.state.display2='';
        console.log("无cardID");
      }
    }
  }
  render() {
    const array1 = [
      {
        text: '绑定',
        onPress: cardid => new Promise((resolve) => {
          //Toast.info('onPress promise', 1);
          setTimeout(() => {
            resolve();
            // console.log(`value:${cardid}`);
            // console.log(this);
            this.boundCard(cardid);
          }, 1000);
        }),
      },
      { text: '取消' },
    ];
    return (
      <List className="UserInstruction_list">
        <div className="instruction-binding-warp">
          <Item wrap>
            <div className="instruction-binding-item">
              <span className="instruction-head">用户须知</span></div>
            <span className="instruction-content">
            一,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目<br />
            二,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目<br />
            三,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目<br />
            四,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目<br />
            五,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目<br />
            六,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目,上海浦东软件园,作为公益服务项目<br />
            </span>
            <div className="user-instruction-button" style={{display:this.state.display1}}>
              <Link to="index/service">
                <Button
                  className="btn1" type="default" inline size="small" onClick={() => {
                  }}
                >不同意</Button>
              </Link>
              <Link to="index/boatOrder">
                <Button
                  className="btn2" type="primary" inline size="small" onClick={() => {
                  }}
                >同意协议</Button>
              </Link>
            </div>
            <div className="instruction-binding-btn" style={{display:this.state.display2}}>
                <Button

                  className="btn" type="primary" inline size="small"
                  onClick={() => prompt('请输入您的卡号', '', array1)}

                >请先绑定卡</Button>
            </div>
          </Item>
        </div>
      </List>
    );
  }
}
export default UserInstruction;
