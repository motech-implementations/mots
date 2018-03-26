import React, { Component } from 'react';
import _ from 'lodash';
import FileDownload from 'js-file-download';
import PropTypes from 'prop-types';
import ReactTable from 'react-table';

import MobileTable from '../components/mobile-table';
import apiClient from '../utils/api-client';
import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';

export default class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      reportData: [],
      reportModel: [],
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
    const url = `api/reports/templates/${this.state.reportId}/json`;

    apiClient.get(url)
      .then((response) => {
        if (response.data && response.data.length === 1) {
          const { colModel, values } = response.data[0];

          if (colModel && colModel.length === 1) {
            const columns = colModel[0];
            const reportData = values || [];
            const reportModel = [];

            _.forEach(columns, (value, key) => {
              reportModel.push({ ...value[0], order: parseInt(value[0].order, 10), accessor: key });
            });

            _.sortBy(reportModel, ['order']);

            this.setState({ reportData, reportModel });
          }
        }
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
        { this.state.reportModel &&
        <div>
          <div className="hide-min-r-small-min">
            <MobileTable
              data={this.state.reportData}
              columns={this.state.reportModel}
            />
          </div>
          <div className="hide-max-r-xsmall-max">
            <ReactTable
              filterable
              data={this.state.reportData}
              columns={this.state.reportModel}
              defaultFilterMethod={(filter, row) => {
                const id = filter.pivotId || filter.id;
                return row[id] !== undefined ? _.includes(row[id], filter.value) : true;
              }}
            />
          </div>
        </div>
        }
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
