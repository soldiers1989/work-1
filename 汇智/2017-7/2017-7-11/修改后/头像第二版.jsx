import React from 'react';
import { List,ImagePicker,Button} from 'antd-mobile';
import { Link } from 'react-router';
import './ActiveDetail.less';
import config from '../../../config';
// import request from '../../../utils/request';
import request from 'superagent';
const image = [];
// const changeUserFaceUrl='http://222.73.203.71:8080/Setting/User/ChangeUserFace/';
const changeUserFaceUrl='http://222.73.203.71:8080/FileUpload/UploadImgComm';

// 头像设置
class part1 extends React.Component {
  state = {
    files: image,
  }
  onChange = (files, type, index) => {
    console.log(files, type, index);
    this.setState({
      files,
    });
  }
  handleImageUpload(file) {
    let upload = request.post(changeUserFaceUrl)
      // .field('upload_preset', 1)
      .field('file', file);
  
    upload.end((err, response) => {
      if (err) {
        console.error(err);
      }

      // if (response.body.secure_url !== '') {
      //   this.setState({
      //     uploadedFileCloudinaryUrl: response.body.secure_url
      //   });
      // }
      console.log(response);
    });
  }
  render() {
    const { files } = this.state;
    return (
      <form className="user_photo">
        <div>
          <ImagePicker
            files={files}
            onChange={this.onChange}
            onImageClick={(index, fs) => console.log(index, fs)}
            selectable={files.length < 1}
          />
        </div>
        <Button className="btn" type="primary" onClick={this.handleImageUpload}>确认上传</Button>
      </form>
    );
  }
}
export default part1;
