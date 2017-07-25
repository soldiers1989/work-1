import React from 'react';
import JsBarcode from 'jsbarcode';
import './Pay.less';//引入样式文件

class CodeBar extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount(){
    //从缓存中读取
    var userInfo = localStorage.userInfo;
    //json转换为Object对象
    var reData = JSON.parse(userInfo);
    //调用条形码生成工具，并将memberCode的值传给条形码
    JsBarcode(this._barcodeSVG, reData.memberCode,
      {
        format: "CODE128",//选择要使用的条形码类型
        displayValue: true,  //  不显示原始值
        // background: '#4b8b7f',  //  背景色
        // lineColor: 'rgba(255,255,255,0.5)', // 线条颜色
        width:4,  // 线条宽度
        height:200,//条形码高度
        fontSize:36//原始值字体大小
      }
    );
  }

  render() {
    return (
      <div className="codeBar_loc">
        <br/>
        <label className="codeBar_lable">扫码付款</label><br/>
        <svg ref={(ref)=>this._barcodeSVG = ref}></svg>
      </div>
    );
  }
}

export default CodeBar;
