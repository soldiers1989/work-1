import React from 'react';
import {Link} from 'react-router';
import { Carousel, WhiteSpace, WingBlank ,Button} from 'antd-mobile';

import './lead.less'

class lead_1 extends React.Component {
  // 定义属性
  static propTypes={

    onStep:React.PropTypes.func,
    onComplete:React.PropTypes.func,
    value:React.PropTypes.number,
    step:React.PropTypes.number
  };

  //这里面的操作可以移动到componentWillMount()里面去
  constructor(...pa) {
    super(...pa);
    this.initValue = this.props.value || 10;
    this.state     = {count: this.initValue}
    this.interval  = 0;
    this.step      = this.props.step || 1;
  }

  stop() {
    clearInterval(this.interval);

  }

  start() {
    this.stop();
    this.interval = setInterval(()=> {
      var count = this.state.count - this.step;
      if (this.props.onStep) {
        this.props.onStep(count);
      }
      if (count ==0) {
        this.props.onComplete && this.props.onComplete();
        this.stop();
        window.location.href="#index/Index";
      }else{
        this.setState({count});
      }

    }, 1000);
  }

  restart() {
    this.stop();
    this.setState({count: this.initValue});
    this.start();
  }
  componentDidMount(){
    this.start();
  }
  componentWillUnmount(){
    this.stop();
  }
  render() {
    return (
      <div>
        <Carousel
          className="my-carousel"
          autoplay={false}
          infinite={false}
          selectedIndex={0}
          swipeSpeed={35}
          beforeChange={(from, to) => console.log(`slide from ${from} to ${to}`)}
          afterChange={index => console.log('slide to', index)}
        >
              <img
                className="lead_img"
                src={require(`../../../assets/lead/lead-p1.jpg`)}
                alt="图片"
              />
              <img
                className="lead_img"
                src={require(`../../../assets/lead/lead-p2.jpg`)}
                alt="图片"
              />
          <div>
            <img
              className="lead_img"
              src={require(`../../../assets/lead/lead-p3.jpg`)}
              alt="图片"

            />
            <Link className="index_link" to="index/Index">
              <div className=" lead_tiYan">
                  <Button className="lead_btn">
                    <span className="lead_tiYan_font">立即体验</span>
                  </Button>
              </div>
            </Link>
          </div>
        </Carousel>

        <Link className="index_link" to="index/Index">
          <div className="lead_skip_bg">
          </div>
          <div className="lead_skip">跳过<br/>&nbsp;<span>{this.state.count}</span>s</div>
        </Link>
      </div>
  );
  }
}

export default lead_1;
