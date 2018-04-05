import React, { Component } from 'react';
import _ from 'lodash';
import FileDownload from 'js-file-download';
import PropTypes from 'prop-types';
import ReactTable from 'react-table';
import { connect } from 'react-redux';
import DateTime from 'react-datetime';

import { resetLogoutCounter } from '../actions/index';
import MobileTable from '../components/mobile-table';
import apiClient from '../utils/api-client';
import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';

class Report extends Component {
  static prepareFilter(parameter) {
    if (!parameter || parameter.dataType === 'String') {
      return null;
    }

    switch (parameter.dataType) {
      case 'Enum':
        return ({ onChange }) => {
          const options = parameter.options.map((option) => {
            const parts = option.split(':');
            return { value: parts[0], displayName: parts.length > 1 ? parts[1] : parts[0] };
          });

          return (
            <select
              onChange={event => onChange(event.target.value)}
              style={{ width: '100%' }}
            >
              <option value="">Show All</option>
              { options.map(option => (
                <option key={option.value} value={option.value}>{option.displayName}</option>
              ))}
            </select>);
        };
      case 'Date':
        return ({ onChange }) => {
          const dateFormat = 'YYYY-MM-DD';

          return (
            <div className="input-group">
              <span className="input-group-addon"><i className="fa fa-calendar" /></span>
              <DateTime
                dateFormat={dateFormat}
                timeFormat={false}
                closeOnSelect
                onChange={date => onChange(!date || typeof date === 'string' ? date : date.format(dateFormat))}
              />
            </div>);
        };
      default:
        return null;
    }
  }

  constructor(props) {
    super(props);
    this.state = {
      reportData: [],
      reportModel: [],
      reportId: '',
      loading: true,
      totalPages: 0,
      orderBy: null,
      filters: {},
      exportWithFilters: false,
      exportWithOrder: false,
    };

    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);

    // flag of someone is typing
    this.filtering = false;

    this.onFilteredChange = this.onFilteredChange.bind(this);
    this.fetchStrategy = this.fetchStrategy.bind(this);

    this.fetchData = this.fetchData.bind(this);
    this.fetchDataWithDebounce = _.debounce(this.fetchData, 500);
  }

  componentWillMount() {
    if (!hasAuthority(DISPLAY_REPORTS_AUTHORITY) || _.isUndefined(this.props.location.state)) {
      this.props.history.push('/home');
    } else {
      this.setState(
        { reportId: this.props.match.params.reportId, loading: true },
        () => this.fetchReport(),
      );
    }
  }

  componentWillReceiveProps(nextProps) {
    this.setState(
      { reportId: nextProps.match.params.reportId, loading: true },
      () => this.fetchReport(),
    );
  }

  onFilteredChange() {
    // when the filter changes, someone is typing
    this.filtering = true;
  }

  getExportParams() {
    const filters = this.state.exportWithFilters ? this.state.filters : {};

    return {
      pageSize: 2147483647,
      offset: 0,
      orderBy: this.state.exportWithOrder ? this.state.orderBy : null,
      ...filters,
    };
  }

  fetchStrategy(tableState) {
    // if someone is typing use debounce
    if (this.filtering) {
      return this.fetchDataWithDebounce(tableState);
    }
    // if not typing (f.ex. sorting) fetch data without debounce
    return this.fetchData(tableState);
  }

  fetchReport() {
    const url = `api/reports/templates/${this.state.reportId}/json`;

    apiClient.get(url, { params: { pageSize: 20 } })
      .then((response) => {
        if (response.data && response.data.length === 1) {
          const { colModel, values, totalPages } = response.data[0];

          if (colModel && colModel.length === 1) {
            const columns = colModel[0];
            const reportModel = [];
            const reportData = values || [];
            const reportTemplate = _.find(this.props.reportTemplates, { id: this.state.reportId });
            const reportParameters = reportTemplate.templateParameters || [];

            _.forEach(columns, (value, key) => {
              const parameter = _.find(reportParameters, { name: key });
              const Filter = Report.prepareFilter(parameter);

              reportModel.push({
                ...value[0],
                order: parseInt(value[0].order, 10),
                accessor: key,
                filterable: !!parameter,
                Filter,
              });
            });

            _.sortBy(reportModel, ['order']);

            this.setState({
              reportModel,
              reportData,
              loading: false,
              totalPages: parseInt(totalPages, 10),
            });
          }
        }
      });
  }

  fetchReportData = (searchParams) => {
    const url = `api/reports/templates/${this.state.reportId}/json`;

    apiClient.get(url, { params: searchParams || {} })
      .then((response) => {
        if (response.data && response.data.length === 1) {
          const { values, totalPages } = response.data[0];
          const reportData = values || [];

          this.setState({
            reportData,
            loading: false,
            totalPages: parseInt(totalPages, 10),
          });
        }
      });
  };

  fetchPdf = () => {
    const url = `api/reports/templates/${this.state.reportId}/pdf`;
    const params = this.getExportParams();

    apiClient({
      url,
      method: 'GET',
      responseType: 'blob',
      params,
    })
      .then((response) => {
        FileDownload(response.data, `${this.props.location.state.reportName}.pdf`);
      });

    this.props.resetLogoutCounter();
  };

  fetchXls = () => {
    const url = `api/reports/templates/${this.state.reportId}/xls`;
    const params = this.getExportParams();

    apiClient({
      url,
      method: 'GET',
      responseType: 'blob',
      params,
    })
      .then((response) => {
        FileDownload(response.data, `${this.props.location.state.reportName}.xls`);
      });

    this.props.resetLogoutCounter();
  };

  fetchData(state) {
    // filtering can be reset
    this.filtering = false;
    const offset = state.page * state.pageSize;
    const sorted = state.sorted.map(order => (`${order.id} ${order.desc ? 'DESC' : 'ASC'}`));
    const orderBy = sorted.join(', ');
    const filters = _.reduce(state.filtered, (acc, { id, value }) =>
      _.assign(acc, { [id]: value }), {});

    this.setState({
      loading: true,
      orderBy,
      filters,
    });

    const searchParams = {
      pageSize: state.pageSize,
      offset,
      ...filters,
      orderBy,
    };
    this.fetchReportData(searchParams);
    this.props.resetLogoutCounter();
  }

  render() {
    return (
      <div >
        <h1 className="page-header padding-bottom-xs margin-x-sm">
          {this.props.location.state ? this.props.location.state.reportName : this.state.reportId}
        </h1>
        <div className="margin-bottom-md">
          <button
            onClick={this.fetchPdf}
            type="button"
            className="btn btn-success margin-left-sm"
          >Download PDF
          </button>
          <button
            onClick={this.fetchXls}
            type="button"
            className="btn btn-success margin-left-sm"
          >Download XLS
          </button>
        </div>
        <div className="form-inline margin-bottom-md">
          <div className="input-group margin-left-sm">
            <input
              id="filtersInput"
              type="checkbox"
              className="checkbox"
              checked={this.state.exportWithFilters}
              onChange={event => this.setState({ exportWithFilters: event.target.checked })}
            />
            <label className="margin-left-sm margin-right-md" htmlFor="filtersInput">
              Export with selected filters
            </label>
          </div>
          <div className="input-group">
            <input
              id="orderInput"
              type="checkbox"
              className="checkbox"
              checked={this.state.exportWithOrder}
              onChange={event => this.setState({ exportWithOrder: event.target.checked })}
            />
            <label className="margin-left-sm margin-right-md" htmlFor="orderInput">
              Export with selected order
            </label>
          </div>
        </div>
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
              manual
              filterable
              loading={this.state.loading}
              data={this.state.reportData}
              columns={this.state.reportModel}
              pages={this.state.totalPages}
              onFetchData={this.fetchStrategy}
              onFilteredChange={this.onFilteredChange}
              getTheadFilterThProps={() => ({ style: { position: 'inherit', overflow: 'inherit' } })}
            />
          </div>
        </div>
        }
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    reportTemplates: state.reports,
  };
}

export default connect(mapStateToProps, { resetLogoutCounter })(Report);

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
  resetLogoutCounter: PropTypes.func.isRequired,
  reportTemplates: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};
