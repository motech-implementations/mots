import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import ReactTable from 'react-table';
import { Link, withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import { fetchChws, resetLogoutCounter } from '../actions/index';
import MobileTable from '../components/mobile-table';
import {
  hasAuthority, CHW_READ_AUTHORITY,
  CHW_WRITE_AUTHORITY, ASSIGN_MODULES_AUTHORITY,
} from '../utils/authorization';
import { buildSearchParams } from '../utils/react-table-search-params';

class ChwTable extends Component {
  constructor() {
    super();
    // flag of someone is typing
    this.filtering = false;

    this.onFilteredChange = this.onFilteredChange.bind(this);
    this.fetchStrategy = this.fetchStrategy.bind(this);

    this.fetchData = this.fetchData.bind(this);
    this.fetchDataWithDebounce = _.debounce(this.fetchData, 500);
  }

  componentWillMount() {
    if (!hasAuthority(CHW_READ_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
  }

  onFilteredChange() {
    // when the filter changes, someone is typing
    this.filtering = true;
  }

  getTableColumns = () => [
    {
      Header: 'Actions',
      minWidth: 70,
      accessor: 'id',
      Cell: cell => (
        <div className="actions-buttons-container">
          { hasAuthority(CHW_WRITE_AUTHORITY) &&
            <Link
              to={`/chw/${cell.value}`}
              type="button"
              className="btn btn-primary margin-right-sm"
              title="Edit"
            >
              <span className="glyphicon glyphicon-edit" />
              <span className="hide-min-r-small-min next-button-text">Edit</span>
            </Link>
            }
          { hasAuthority(ASSIGN_MODULES_AUTHORITY) &&
            <Link
              to={`/modules/assign/${cell.value}`}
              type="button"
              className="btn btn-success"
              title="Assign Module"
            >
              <span className="glyphicon glyphicon-circle-arrow-right" />
              <span
                className="hide-min-r-small-min next-button-text"
              >Assign Module
              </span>
            </Link>
            }
        </div>

      ),
      filterable: false,
      sortable: false,
      show: this.props.selected && (hasAuthority(CHW_WRITE_AUTHORITY) ||
          hasAuthority(ASSIGN_MODULES_AUTHORITY)),
    },
    {
      Header: 'ID',
      accessor: 'chwId',
    }, {
      Header: 'First name',
      accessor: 'firstName',
    }, {
      Header: 'Surname',
      accessor: 'secondName',
    }, {
      Header: 'Other name',
      accessor: 'otherName',
    }, {
      Header: 'YOB',
      accessor: 'yearOfBirth',
      filterable: false,
    }, {
      Header: 'Gender',
      accessor: 'gender',
      filterable: false,
    }, {
      Header: 'Education level',
      accessor: 'educationLevel',
      Filter: ({ onChange }) => (
        <select
          onChange={event => onChange(event.target.value)}
          style={{ width: '100%' }}
        >
          <option value="">Show All</option>
          <option value="pre_primary">Pre-primary</option>
          <option value="primary">Primary</option>
          <option value="junior_secondary">Junior Secondary</option>
          <option value="secondary">Secondary</option>
          <option value="senior_secondary">Senior Secondary</option>
          <option value="higher">Higher</option>
          <option value="university">University</option>
          <option value="none">None</option>
        </select>),
    }, {
      Header: 'Literacy',
      accessor: 'literacy',
      filterable: false,
    }, {
      Header: 'District',
      accessor: 'districtName',
    }, {
      Header: 'Chiefdom',
      accessor: 'chiefdomName',
    }, {
      Header: 'Facility',
      accessor: 'facilityName',
    }, {
      Header: 'Community',
      accessor: 'communityName',
    }, {
      Header: 'PHU Supervisor',
      accessor: 'phuSupervisor',
    }, {
      Header: 'Preferred language',
      accessor: 'preferredLanguage',
      filterable: false,
    }, {
      Header: 'Phone number',
      accessor: 'phoneNumber',
    }, {
      Header: 'Group',
      accessor: 'groupName',
    }];

  prepareMobileColumns() {
    const mobileColumns = _.clone(this.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  fetchStrategy(tableState) {
    // if someone is typing use debounce
    if (this.filtering) {
      return this.fetchDataWithDebounce(tableState);
    }
    // if not typing (f.ex. sorting) fetch data without debounce
    return this.fetchData(tableState);
  }

  fetchData(tableState) {
    // filtering can be reset
    this.filtering = false;

    this.setState({ loading: true });

    this.props.fetchChws(buildSearchParams(
      tableState.filtered,
      tableState.sorted,
      tableState.page,
      tableState.pageSize,
    ), this.props.selected)
      .then(() => {
        this.setState({ loading: false });
      });

    this.props.resetLogoutCounter();
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable data={this.props.chwList} columns={this.prepareMobileColumns()} />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable
            manual
            filterable
            data={this.props.chwList}
            pages={this.props.chwListPages}
            columns={this.getTableColumns()}
            loading={this.state.loading}
            onFetchData={this.fetchStrategy}
            onFilteredChange={this.onFilteredChange}
          />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    chwList: state.tablesReducer.chwList,
    chwListPages: state.tablesReducer.chwListPages,
  };
}

export default withRouter(connect(mapStateToProps, {
  fetchChws, resetLogoutCounter,
})(ChwTable));

ChwTable.propTypes = {
  fetchChws: PropTypes.func.isRequired,
  chwListPages: PropTypes.number.isRequired,
  chwList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  selected: PropTypes.bool,
  resetLogoutCounter: PropTypes.func.isRequired,
};

ChwTable.defaultProps = {
  selected: true,
};
