import React from 'react';
import { Router, Route, IndexRoute } from 'dva/router';
import Login from './routes/main/Login';
import ForgetPwdOne from './routes/main/ForgetPwdOne';
import ForgetPwdTwo from './routes/main/ForgetPwdTwo';
import RegisterStepOne from './routes/main/RegisterStepOne';
import RegisterStepTwo from './routes/main/RegisterStepTwo';
import ChangePwd from './routes/main/ChangePwd';
import Index from './routes/main/Index';
import ActiveDetail from './routes/main/ActiveDetail';
import ActiveCenter from './routes/main/ActiveCenter';
import MyXiaozhi from './routes/main/MyXiaozhi';

import ApplyCard from './routes/card/ApplyCard';
import TransactionQuery from './routes/card/TransactionQuery';
import Recharge from './routes/card/Recharge';
import RechargeTwo from './routes/card/RechargeTwo';
import RechargeThree from './routes/card/RechargeThree';
import Unbunding from './routes/card/Unbunding';
import Unbound from './routes/card/Unbound';
import Bound from './routes/card/Bound';
import Bunding from './routes/card/Bunding';

import PersonInfo from './routes/user/PersonInfo';
import ChangePhoneNo from './routes/user/ChangePhoneNo';
import PersonCenter from './routes/user/PersonCenter';
import PersonNotify from './routes/user/PersonNotify';
import UserPay from './routes/user/UserPay';
import UserInfo from './routes/user/UserInfo';

import PropertyRepair from './routes/server/PropertyRepair';
import UserInstruction from './routes/server/UserInstruction';
import InstructionBinding from './routes/server/InstructionBinding';
import BoatOrder from './routes/server/BoatOrder';
import Service from './routes/server/ServiceCenter';
import ITRepair from './routes/server/ITRepair';

import Wrap from './components/Wrap';
import WrapTab from './components/WrapTab';
import Personnel from './components/Personnel';

import  Welcome from './routes/main/Welcome'

// 管理所有页面的路由功能
function RouterConfig({ history }) {
  return (
    <Router history={history}>
      <Route path="/">
        <IndexRoute component={Welcome} />
      </Route>
      <Route path="/" component={Wrap}>
        <Route path="login" breadName="登录" component={Login} />
        <Route path="ForgetPwdOne" breadName="忘记密码" component={ForgetPwdOne} />
        <Route path="ForgetPwdTwo" breadName="忘记密码" component={ForgetPwdTwo} />
        <Route path="RegisterStepOne" breadName="注册" component={RegisterStepOne} />
        <Route path="RegisterStepTwo" breadName="注册" component={RegisterStepTwo} />

        <Route path="ChangePwd" breadName="登录密码修改" component={ChangePwd} />
        <Route path="ChangePhoneNo" breadName="联系方式修改" component={ChangePhoneNo} />
        <Route path="ApplyCard" breadName="申请汇智卡" component={ApplyCard} />
        <Route path="TransactionQuery" breadName="交易明细" component={TransactionQuery} />
        <Route path="Recharge" breadName="充值" component={Recharge} />
        <Route path="RechargeTwo" breadName="充值" component={RechargeTwo} />
        <Route path="RechargeThree" breadName="充值" component={RechargeThree} />

        <Route path="/index" component={WrapTab}>
          <Route path="Index" breadName="首页" component={Index} />
          <Route path="service" breadName="服务" component={Service} />

          <Route path="MyXiaozhi" breadName="设置" component={MyXiaozhi} />

          <Route path="propertyRepair" breadName="物业报修" component={PropertyRepair} />
          <Route path="instruction" component={UserInstruction} />
          <Route path="instructionBinding" component={InstructionBinding} />
          <Route path="boatOrder" breadName="游船预约" component={BoatOrder} />
          <Route path="unbunding" breadName="解绑" component={Unbunding} />
          <Route path="unbound" breadName="汇智卡未绑定" component={Unbound} />
          <Route path="bound" breadName="汇智卡已绑定" component={Bound} />
          <Route path="bunding" breadName="绑定" component={Bunding} />
          <Route path="itRepair" breadName="IT报修" component={ITRepair} />

          <Route path="userinfo" component={UserInfo} />
          <Route path="personnel" component={Personnel} />

          <Route path="activedetail" breadName="活动详情" component={ActiveDetail} />
          <Route path="ActiveCenter" breadName="活动中心" component={ActiveCenter} />
          <Route path="personinfo" breadName="个人信息" component={PersonInfo} />
          <Route path="personcenter" breadName="个人中心" component={PersonCenter} />
          <Route path="personnotify" breadName="个人消息中心" component={PersonNotify} />
          <Route path="UserPay" breadName="支付记录" component={UserPay} />
        </Route>
      </Route>
    </Router>
  );
}
export default RouterConfig;
