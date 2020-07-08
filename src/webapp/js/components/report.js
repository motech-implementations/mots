import React, { Component } from 'react';
import _ from 'lodash';
import FileDownload from 'js-file-download';
import PropTypes from 'prop-types';
import ReactTable from 'react-table';
import { connect } from 'react-redux';
import DateTime from 'react-datetime';
import moment from 'moment';

import { resetLogoutCounter } from '../actions/index';
import MobileTable from '../components/mobile-table';
import apiClient from '../utils/api-client';
import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';

class Report extends Component {
  static prepareFilter(parameter) {
    if (!parameter) {
      return null;
    }

    switch (parameter.dataType) {
      case 'String':
        return ({ filter, onChange }) => (
          <input
            className="form-control"
            onChange={event => onChange(event.target.value)}
            value={filter ? filter.value : ''}
          />
        );
      case 'Enum':
        return ({ filter, onChange }) => {
          const options = parameter.options.map((option) => {
            const parts = option.split(':');
            return { value: parts[0], displayName: parts.length > 1 ? parts[1] : parts[0] };
          });

          return (
            <select
              onChange={event => onChange(event.target.value)}
              style={{ width: '100%' }}
              value={filter ? filter.value : ''}
            >
              <option value="">Show All</option>
              { options.map(option => (
                <option key={option.value} value={option.value}>{option.displayName}</option>
              ))}
            </select>);
        };
      case 'Date':
        return ({ filter, onChange }) => {
          const dateFormat = 'YYYY-MM-DD';

          return (
            <div>
              <DateTime
                dateFormat={dateFormat}
                timeFormat={false}
                closeOnSelect
                onChange={date => onChange({
                  type: 'date',
                  min: !date || typeof date === 'string' ? date : date.format(dateFormat),
                  max: filter && filter.value ? filter.value.max : null,
                })}
                value={filter && filter.value ? filter.value.min : null}
                isValidDate={(current) => {
                  const max = filter && filter.value ? filter.value.max : null;

                  if (!max || !moment(max).isValid()) {
                    return true;
                  }

                  return !current || current.isSameOrBefore(moment(max));
                }}
                renderInput={props => (
                  <div className="input-group">
                    <span className="input-group-addon"><i className="fa fa-calendar" /></span>
                    <input {...props} placeholder="Min Date" />
                  </div>
                )}
              />
              <DateTime
                dateFormat={dateFormat}
                timeFormat={false}
                closeOnSelect
                onChange={date => onChange({
                  type: 'date',
                  max: !date || typeof date === 'string' ? date : date.format(dateFormat),
                  min: filter && filter.value ? filter.value.min : null,
                })}
                value={filter && filter.value ? filter.value.max : null}
                isValidDate={(current) => {
                  const min = filter && filter.value ? filter.value.min : null;

                  if (!min || !moment(min).isValid()) {
                    return true;
                  }

                  return !current || current.isSameOrAfter(moment(min));
                }}
                renderInput={props => (
                  <div className="input-group">
                    <span className="input-group-addon"><i className="fa fa-calendar" /></span>
                    <input {...props} placeholder="Max Date" />
                  </div>
                )}
              />
            </div>);
        };
      default:
        return null;
    }
  }

  static convertFilters(filtered) {
    return _.reduce(filtered, (acc, { id, value }) => {
      if (value && value.type === 'date') {
        return {
          ...acc,
          [`${id}Min`]: value.min,
          [`${id}Max`]: value.max,
        };
      }

      return { ...acc, [id]: value };
    }, {});
  }

  static convertOrder(sorted) {
    const orders = sorted.map(order => (`${order.id} ${order.desc ? 'DESC' : 'ASC'}`));
    return orders.join(', ');
  }

  constructor(props) {
    super(props);
    this.state = {
      reportData: [],
      reportModel: [],
      reportId: '',
      loading: true,
      totalPages: 0,
      filtered: [],
      sorted: [],
      page: 0,
      pageSize: 20,
      exportWithFilters: false,
      exportWithOrder: false,
    };

    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);

    this.fetchData = this.fetchData.bind(this);
    this.fetchDataWithDebounce = _.debounce(this.fetchData, 500);
  }

  componentDidMount() {
    if (!hasAuthority(DISPLAY_REPORTS_AUTHORITY) || _.isUndefined(this.props.location.state)) {
      this.props.history.push('/home');
    } else {
      this.setState(
        {
          reportId: this.props.match.params.reportId,
          loading: true,
          filtered: [],
          sorted: [],
          page: 0,
          pageSize: 20,
          exportWithFilters: false,
          exportWithOrder: false,
        },
        () => this.fetchReport(),
      );
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.props.match.params.reportId !== prevState.reportId) {
      // eslint-disable-next-line react/no-did-update-set-state
      this.setState(
        {
          reportId: this.props.match.params.reportId,
          loading: true,
          filtered: [],
          sorted: [],
          page: 0,
          pageSize: 20,
          exportWithFilters: false,
          exportWithOrder: false,
        },
        () => this.fetchReport(),
      );
    }
  }

  getExportParams() {
    const filters = this.state.exportWithFilters ? Report.convertFilters(this.state.filtered) : {};
    const orderBy = this.state.exportWithOrder ? Report.convertOrder(this.state.sorted) : null;

    return {
      pageSize: 2147483647,
      offset: 0,
      orderBy,
      ...filters,
    };
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
                minWidth: parameter && parameter.dataType === 'Date' ? 150 : 100,
                Filter,
              });
            });

            _.sortBy(reportModel, ['order']);
            reportModel.sort((o1, o2) => o1.order - o2.order);

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

  fetchData() {
    const offset = this.state.page * this.state.pageSize;
    const orderBy = Report.convertOrder(this.state.sorted);
    const filters = Report.convertFilters(this.state.filtered);

    this.setState({ loading: true });

    const searchParams = {
      pageSize: this.state.pageSize,
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
              filtered={this.state.filtered}
              sorted={this.state.sorted}
              page={this.state.page}
              pageSize={this.state.pageSize}
              onFilteredChange={
                filtered => this.setState({ filtered, page: 0 }, this.fetchDataWithDebounce)}
              onSortedChange={sorted => this.setState({ sorted, page: 0 }, this.fetchData)}
              onPageChange={page => this.setState({ page }, this.fetchData)}
              onPageSizeChange={pageSize => this.setState({ pageSize, page: 0 }, this.fetchData)}
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
