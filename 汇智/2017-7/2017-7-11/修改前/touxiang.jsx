import React from 'react';
import { List,ImagePicker,Button} from 'antd-mobile';
import { Link } from 'react-router';
import './ActiveDetail.less';
import config from '../../../config';
const data = [{
  url: 'http://localhost:8000/static/user-test.5749acdd.png',
  id: '2121',
  webkitRelativePath:config.uploadImgUrl,
}];

// 头像设置
class part1 extends React.Component {
  state = {
    files: data,
  }
  onChange = (files, type, index) => {
    console.log(files, type, index);
    this.setState({
      files,
    });
  }
  render() {
    const { files } = this.state;
    return (
      <div className="user_photo">
        <div>
          <ImagePicker
            files={files}
            webkitRelativePath={files.webkitRelativePath}
            onChange={this.onChange}
            onImageClick={(index, fs) => console.log(index, fs)}
            selectable={files.length < 2}
          />
        </div>
        <Button className="btn" type="primary">确认上传</Button>
      </div>
    );
  }
}
export default part1;
