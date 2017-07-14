import React from 'react';
import { List, Switch, WingBlank, Card, Flex, Tag, Icon, Tabs } from 'antd-mobile';
import { createForm } from 'rc-form';
import { Link } from 'react-router';

const TabPane = Tabs.TabPane;

// 红包
class part1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal1: false,
    };
  }

  render() {
    const { getFieldProps } = this.props.form;

    return (
      <form>
        <List>
          <img style={{width:'100%',height:'auto'}} src={require('../../assets//mine/mine-p1.jpg')} alt="图片" />
        </List>
        <WingBlank
          style={{
            marginTop: '5px',
          }} size="sm"
        >
          <List>
            <Card>
              <Card.Body>
                <Tabs>
                  <TabPane tab="可用红包" key="1">
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '5rem', backgroundColor: '#fff' }}>
                      <List
                        className="red-packet-list"
                        style={{ backgroundColor: 'white' }}
                      >
                        <div className="packet" style={{display:'block', alignItems: 'center', justifyContent: 'center', height: '1.5rem', width: '6rem', backgroundColor: '#f00' }}>
                          <div className="packet-money" style={{ alignItems: 'center', justifyContent: 'center', height:'1rem', width: '6rem',marginTop:'20px'}}>
                            <span className="yuan" style={{color: '#fff', fontSize: '0.4rem', lineHeight: '65px',marginLeft:'30px'}}>¥</span><span className="big" style={{color: '#fff', fontSize: '0.5rem', lineHeight: '65px'}}>49.7</span><span style={{color: '#fff', fontSize: '0.4rem', lineHeight: '65px'}}>元</span>
                          </div>
                          <div className="packet-time" style={{ alignItems: 'center', justifyContent: 'center', height: '0.4rem', width: '6rem',padding: '0px 10px 8px',lineHeight: '25px'}}>有效期:2016.11.22-2017.11.23
                          </div>
                        </div>
                      </List>
                    </div>
                  </TabPane>
                  <TabPane tab="已用/过期红包" key="2">
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '5rem', backgroundColor: '#fff' }}>
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
