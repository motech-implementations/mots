import React, { Component } from 'react';
import _ from 'lodash';
import FileDownload from 'js-file-download';
import PropTypes from 'prop-types';
import DOMPurify from 'dompurify';
import apiClient from '../utils/api-client';
import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';

/* eslint-disable react/no-danger */
export default class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      reportHtml: '',
      reportId: '',
    };

    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(DISPLAY_REPORTS_AUTHORITY) || _.isUndefined(this.props.location.state)) {
      this.props.history.push('/home');
    } else {
      this.setState({ reportId: this.props.match.params.reportId }, () => this.fetchReport());
    }
  }

  componentWillReceiveProps(nextProps) {
    this.setState({ reportId: nextProps.match.params.reportId }, () => this.fetchReport());
  }

  fetchReport = () => {
    const url = `api/reports/templates/${this.state.reportId}/html`;

    apiClient.get(url)
      .then((response) => {
        const cleanHtml = DOMPurify.sanitize(response.data);
        this.setState({ reportHtml: cleanHtml });
      });
  };

  fetchPdf = () => {
    const url = `api/reports/templates/${this.state.reportId}/pdf`;

    apiClient({
      url,
      method: 'GET',
      responseType: 'blob',
    })
      .then((response) => {
        FileDownload(response.data, `${this.props.location.state.reportName}.pdf`);
      });
  };

  fetchXls = () => {
    const url = `api/reports/templates/${this.state.reportId}/xls`;

    apiClient({
      url,
      method: 'GET',
      responseType: 'blob',
    })
      .then((response) => {
        FileDownload(response.data, `${this.props.location.state.reportName}.xls`);
      });
  };

  render() {
    return (
      <div >
        <h1 className="page-header padding-bottom-xs margin-x-sm">
          {this.props.location.state ? this.props.location.state.reportName : this.state.reportId}
        </h1>
        <button
          onClick={this.fetchPdf}
          type="button"
          className="btn btn-success margin-left-sm margin-bottom-md"
        >Download PDF
        </button>
        <button
          onClick={this.fetchXls}
          type="button"
          className="btn btn-success margin-left-sm margin-bottom-md"
        >Download XLS
        </button>
        <div dangerouslySetInnerHTML={{ __html: this.state.reportHtml }} />
      </div>
    );
  }
}

Report.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({ reportId: PropTypes.string }),
  }).isRequired,
  location: PropTypes.shape({
    state: PropTypes.shape({ reportName: PropTypes.string }),
  }).isRequired,
};
