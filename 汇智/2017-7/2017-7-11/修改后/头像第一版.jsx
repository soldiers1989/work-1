import React from 'react';
import { List,ImagePicker,Button} from 'antd-mobile';
import { Link } from 'react-router';
import './ActiveDetail.less';
import config from '../../../config';
import Dropzone from 'react-dropzone';
import request from 'superagent';
const data = [{
  url: 'http://localhost:8000/static/user-test.5749acdd.png',
  id: '2121',
}];
const CLOUDINARY_UPLOAD_PRESET = 'your_upload_preset_id';
const CLOUDINARY_UPLOAD_URL ='http://222.73.203.71:8080/FileUpload/UploadImgComm';
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
  constructor(props) {
    super(props);

    this.state = {
      uploadedFileCloudinaryUrl: 'http://localhost:8000/static/user-test.5749acdd.png'
    };
  }
  handleImageUpload(file) {
    let upload = request.post(CLOUDINARY_UPLOAD_URL)
      .field('upload_preset', CLOUDINARY_UPLOAD_PRESET)
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
      <div className="user_photo">
        <div>
          <Dropzone
            multiple={false}
            accept="image/*">
            <p>点击上传图片</p>
          </Dropzone>
          <div>
            {this.state.uploadedFileCloudinaryUrl === '' ? null :
              <div>
                <img src={this.state.uploadedFileCloudinaryUrl} />
              </div>}
          </div>
        </div>
        <Button className="btn" type="primary" onClick={this.handleImageUpload}>确认上传</Button>
      </div>
    );
  }
}
export default part1;
