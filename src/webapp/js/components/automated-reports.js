import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import ReactTable from 'react-table';
import { initialize } from 'redux-form';
import FileDownload from 'js-file-download';

import {
  AUTOMATED_REPORT_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import { fetchAutomatedReports } from '../actions/index';
import { AUTOMATED_REPORT_SETTINGS_FORM_NAME } from './automated-settings-edit';
import apiClient from '../utils/api-client';

class AutomatedReports extends Component {
  componentDidMount() {
    this.props.fetchAutomatedReports();
  }

  getReport = (name) => {
    const { automatedReports } = this.props;
    return automatedReports ? automatedReports.find(report => report.jobName === name) : [];
  };

  initializeForm = (data) => {
    const date = new Date(Date.parse(data.startDate == null ? new Date() : data.startDate));
    this.props.initialize(AUTOMATED_REPORT_SETTINGS_FORM_NAME, { ...data, startDate: date });
  };

  fetchPdf = (templateId, templateName) => {
    const url = `api/reports/templates/${templateId}/pdf`;
    const params = {
      logo: '/reports/ebodac_logo.jpg',
    };

    apiClient({
      url,
      method: 'GET',
      responseType: 'blob',
      params,
    })
      .then((response) => {
        FileDownload(response.data, `${templateName}.pdf`);
      });
  };

  getTableColumns = () => [
    {
      Header: 'Job name',
      accessor: 'jobName',
    }, {
      Header: 'Start date',
      accessor: 'startDate',
    }, {
      Header: 'Period',
      accessor: 'period',
    }, {
      Header: 'Enabled',
      accessor: 'enabled',
      Cell: cell => (
        <div className=" actions-buttons-container">
          <span className={`checkbox-icon ${cell.value ? 'fa fa-check-square' : 'fa fa-times-circle'}`} />
        </div>
      ),
    }, {
      Header: 'Actions',
      accessor: 'jobName',
      Cell: cell => (
        <div className="actions-buttons-container">
          { hasAuthority(AUTOMATED_REPORT_AUTHORITY)
          && (
            <div>
              <Link
                to={{
                  pathname: 'automatedReportsEdit',
                  state: {
                    downloadPdf: () => this.fetchPdf(this.getReport(cell.value)
                      .templateId, cell.value),
                  },
                }}
                onClick={() => this.initializeForm(this.getReport(cell.value))}
                type="button"
                className="btn btn-primary margin-right-sm"
                title="Edit"
              >
                <span className="fa fa-edit" />
                <span className="hide-min-r-small-min next-button-text">Edit</span>
              </Link>
              <button
                onClick={() => this.fetchPdf(this.getReport(cell.value)
                  .templateId, cell.value)}
                type="button"
                className="btn btn-success margin-right-sm"
              >
                Download PDF
              </button>
            </div>
          )}
        </div>
      ),
    }];

  render() {
    const { automatedReports } = this.props;
    return (
      <div className="hide-max-r-xsmall-max">
        <h1 className="page-header padding-bottom-xs margin-x-sm">Automated Reports</h1>
        <ReactTable
          filterable
          data={automatedReports}
          columns={this.getTableColumns()}
        />
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    automatedReports: state.automatedReports.automatedReports.data,
  };
}

export default connect(mapStateToProps, {
  fetchAutomatedReports,
  initialize,
})(AutomatedReports);

AutomatedReports.propTypes = {
  automatedReports: PropTypes.arrayOf(PropTypes.shape({
  })),
  fetchAutomatedReports: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
};

AutomatedReports.defaultProps = {
  automatedReports: [],
};
