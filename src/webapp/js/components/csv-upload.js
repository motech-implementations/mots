import React, { Component } from 'react';
import { initialize } from 'redux-form';
import { connect } from 'react-redux';
import _ from 'lodash';
import { ClimbingBoxLoader } from 'react-spinners';
import PropTypes from 'prop-types';

import apiClient from '../utils/api-client';
import { resetLogoutCounter } from '../actions/index';


class CsvUpload extends Component {
  constructor(props) {
    super(props);
    this.state = {
      file: null,
      loading: false,
      filename: '',
      selected: false,
    };

    this.onFormSubmit = this.onFormSubmit.bind(this);
    this.onChange = this.onChange.bind(this);
    this.fileUpload = this.fileUpload.bind(this);
  }

  onFormSubmit(e) {
    e.preventDefault();
    this.fileUpload(this.state.file);
  }

  onChange(e) {
    const file = e.target.files[0];
    this.setState({ file, filename: file.name });
  }

  fileUpload(file) {
    this.setState({ loading: true, content: '' });
    const formData = new FormData();
    formData.append('file', file);
    const config = {
      headers: {
        'content-type': 'multipart/form-data',
      },
    };

    const url = `${this.props.uploadUrl}/${this.state.selected}`;

    apiClient.post(url, formData, config).then((response) => {
      let content = `Your upload has been successful! \nThere were ${_.size(response.data)} issue/s with your file. \n`;

      _.map(response.data, (value, key) => {
        content += `CSV row ${key}: ${value} \n`;
      });

      this.setState({ content, loading: false });
    });
  }

  render() {
    return (
      <div>
        <div className="padding-bottom-sm">
          <h1>CSV File Upload</h1>
        </div>
        <div>
          <form className="form-horizontal" onSubmit={this.onFormSubmit} >
            <div className="row padding-bottom-xs">
              <div className="input-group col-md-4">
                <label className="input-group-btn" htmlFor="csvInput">
                  <span className="btn btn-primary">
                    Browse <input
                      id="csvInput"
                      type="file"
                      style={{ display: 'none' }}
                      onChange={this.onChange}
                      accept=".csv"
                      className="btn btn-primary"
                      onClick={() => this.props.resetLogoutCounter()}
                    />
                  </span>
                </label>
                <input
                  type="text"
                  className="form-control"
                  readOnly
                  value={this.state.filename}
                />
              </div>
            </div>
            <div className="row">
              <div className="input-group margin-top-md margin-bottom-md">
                <input
                  id="selectedInput"
                  type="checkbox"
                  className="checkbox-inline"
                  checked={this.state.selected}
                  onChange={event => this.setState({ selected: event.target.checked })}
                />
                <label className="margin-left-sm" htmlFor="selectedInput">
                  {this.props.selectText}
                </label>
              </div>
            </div>
            <div className="row">
              <button
                type="submit"
                className="col-md-4 offset-md-2 btn btn-primary"
                disabled={!this.state.file}
                onClick={() => this.props.resetLogoutCounter()}
              >Upload
              </button>
            </div>
          </form>
        </div>
        <h3>Result</h3>
        <div>
          <ClimbingBoxLoader
            color="#000000"
            loading={this.state.loading}
          />
        </div>
        <div className="preformatted">
          {this.state.content}
        </div>
      </div>
    );
  }
}

export default connect(null, { initialize, resetLogoutCounter })(CsvUpload);

CsvUpload.propTypes = {
  uploadUrl: PropTypes.string.isRequired,
  selectText: PropTypes.string.isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
