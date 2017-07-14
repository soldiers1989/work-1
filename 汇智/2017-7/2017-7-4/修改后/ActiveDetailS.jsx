import React from 'react';
import { List,ImagePicker,Button} from 'antd-mobile';
import { Link } from 'react-router';

import ActiveDetail_1 from "../../components/main/ActiveDetail/part1";

class ActiveDetail extends React.Component {
  render() {
    return (
      <div className="ActiveDetail">
        <ActiveDetail_1/>
      </div>
    );
  }
}

export default ActiveDetail;
