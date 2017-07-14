import React from 'react';
import { List, Button } from 'antd-mobile';
import { Link } from 'react-router';
const Item = List.Item;
import './binding.less';
class InstructionBinding extends React.Component {
  render() {
    return (
      <List className="instruction_list">
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
            </span><div>
            <div className="instruction-binding-btn">
              <Link to="index/bunding">
                <Button
                  className="btn" type="primary" inline size="small" onClick={() => {
                }}
                >请先绑定卡</Button>
              </Link>
            </div>
          </div>
          </Item>
        </div>
      </List>
    );
  }
}
export default InstructionBinding;
