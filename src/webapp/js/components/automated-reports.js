import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import ReactTable from 'react-table';
import { initialize } from 'redux-form';

import {
  AUTOMATED_REPORT_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import { fetchAutomatedReports } from '../actions/index';
import { AUTOMATED_REPORT_SETTINGS_FORM_NAME } from './automated-settings-edit';

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

  getTableColumns = () => [
    {
      Header: 'Job name',
      accessor: 'jobName',
    }, {
      Header: 'Start date',
      accessor: 'startDate',
    }, {
      Header: 'Interval in seconds',
      accessor: 'intervalInSeconds',
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
            <Link
              to={{
                pathname: 'automatedReportsEdit',
              }}
              onClick={() => this.initializeForm(this.getReport(cell.value))}
              type="button"
              className="btn btn-primary margin-right-sm"
              title="Edit"
            >
              <span className="fa fa-edit" />
              <span className="hide-min-r-small-min next-button-text">Edit</span>
            </Link>
          )}
        </div>
      ),
    }];

  render() {
    const { automatedReports } = this.props;
    return (
      <div className="hide-max-r-xsmall-max">
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
