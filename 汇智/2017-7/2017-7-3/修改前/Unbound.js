import React from 'react';
import { Link } from 'react-router';
import { Card,WhiteSpace,WingBlank,Button,Grid,Icon} from 'antd-mobile';

const data = Array.from(new Array(9)).map((_val, i) => ({
  icon: 'https://os.alipayobjects.com/rmsportal/IptWdCkrtkAUfjE.png',
  text: `name${i}`,
}));

class Unbound extends React.Component {
  render() {
    return (
      <div style={{ marginTop: '20px' }}>

        <Link to="index/Index">
          <div className="index_part1">
            <img style={{width:'100%',height:'auto'}} src={require('../../assets/card/card-p1.jpg')} />
          </div>
        </Link>


        <Card style={{border:'none'}} className="Unbound_part2">
          <div style={{textAlign:'center',marginTop:'6%'}}>
            <Button type="primary" inline size="small"
                    style={{ marginRight: '1rem',backgroundColor: '#f76c07',padding: '10px' }}>
              绑定汇智卡
            </Button>
            <Button  type="primary" inline size="small"
                     style={{ backgroundColor: '#f76c07',padding: '10px' }}>
              申请汇智卡
            </Button>
          </div>


        </Card>



        <Card className=" Unbound_part2" style={{border:'none'}}>
          <div >
            <div className="Unbound_content ">
              <ul className="Unbound_content_ul clearfix">

                <Link className="Unbound_link">
                  <li>

                    <Icon type={require('../../assets/card/card-recharge.svg')} className="tabSelect-icon" />
                    {/*<img src={require('../assets/bound2.png')} />*/}
                    <p>充值</p>
                  </li>
                </Link>
                <Link className="Unbound_link">
                  <li className="two" onClick={{}}>
                    <Icon type={require('../../assets/card/card-detail.svg')} className="tabSelect-icon" />
                    {/*<img src={require('../assets/bound3.png')} />*/}
                    <p>交易明细查询</p>
                  </li>
                </Link>
                <Link className="Unbound_link">
                  <li>
                    <Icon type={require('../../assets/card/card-unbund.svg')} className="tabSelect-icon" />
                    {/*<img src={require('../assets/bound4.png')} />*/}
                    <p>解绑</p>
                  </li>
                </Link>
                <Link className="Unbound_link">
                  <li>
                    <Icon type={require('../../assets/card/card-unhang.svg')} className="tabSelect-icon" />
                    {/*<img src={require('../assets/bound5.png')} />*/}
                    <p>挂失</p>
                  </li>
                </Link>
                <Link className="Unbound_link">
                  <li  className="two">
                    <Icon type={require('../../assets/card/card-lose.svg')} className="tabSelect-icon" />
                    {/*<img src={require('../assets/bound6.png')} />*/}
                    <p>解挂</p>
                  </li>
                </Link>
                <Link className="Unbound_link">
                  <li>
                    <Icon type={require('../../assets/card/card-red.svg')} className="tabSelect-icon" />
                    {/*<img src={require('../assets/bound7.png')} />*/}
                    <p>红包管理</p>
                  </li>
                </Link>
              </ul>
            </div>
          </div>


        </Card>


        {/*------------------------------第三部分-----------------汇智卡使用帮助-------------------*/}

        <Card className="Unbound_part2" style={{border:'none'}}>
          <Card.Header
            title="汇智卡使用帮助"
            thumb={require('../../assets/card/card-qmark.png')}
            // thumb={require('../../assets/card-qmark.svg')}
          />
          <Card.Body>
            <div>
              <Grid data={data} columnNum={7} hasLine={false} />
            </div>
          </Card.Body>

        </Card>
      </div>


    );
  }
}
export default Unbound;
