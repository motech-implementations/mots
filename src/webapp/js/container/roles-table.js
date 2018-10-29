import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { searchRoles, resetLogoutCounter } from '../actions/index';
import { buildSearchParams } from '../utils/react-table-search-params';

class RolesTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(RolesTable.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

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
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
  }

  onFilteredChange() {
    // when the filter changes, someone is typing
    this.filtering = true;
  }

  static getTableColumns = () => [
    {
      Header: 'Actions',
      minWidth: 50,
      accessor: 'id',
      Cell: cell => (
        <div className="actions-buttons-container disabled">
          <Link
            to={`/roles/${cell.value}`}
            type="button"
            className={`btn btn-primary margin-right-sm ${cell.original && cell.original.readonly ? 'disabled' : ''}`}
            title="Edit"
          >
            <span className="glyphicon glyphicon-edit" />
            <span className="hide-min-r-small-min next-button-text">Edit</span>
          </Link>
        </div>
      ),
      filterable: false,
      sortable: false,
    },
    {
      Header: 'Name',
      accessor: 'name',
    }, {
      Header: 'Permissions',
      accessor: 'permissions',
      Cell: cell => (
        <div className="actions-buttons-container">
          {_.join(_.map(cell.value, val => val.displayName), ', ')}
        </div>
      ),
      filterable: false,
      sortable: false,
    }];

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

    const filters = tableState.filtered.map(a => ({ ...a }));
    const sorting = tableState.sorted.map(a => ({ ...a }));

    this.setState({ loading: true });
    this.props.searchRoles(buildSearchParams(
      filters,
      sorting,
      tableState.page,
      tableState.pageSize,
    ))
      .then(() => {
        this.setState({ loading: false });
      });

    this.props.resetLogoutCounter();
  }


  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable
            data={this.props.filteredRoles}
            columns={RolesTable.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable
            manual
            filterable
            data={this.props.filteredRoles}
            pages={this.props.rolesTotalPages}
            columns={RolesTable.getTableColumns()}
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
    filteredRoles: state.tablesReducer.filteredRoles,
    rolesTotalPages: state.tablesReducer.rolesTotalPages,
  };
}

export default withRouter(connect(mapStateToProps, {
  searchRoles, resetLogoutCounter,
})(RolesTable));

RolesTable.propTypes = {
  searchRoles: PropTypes.func.isRequired,
  filteredRoles: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  rolesTotalPages: PropTypes.number.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
