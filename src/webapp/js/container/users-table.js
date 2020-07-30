import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { fetchUsers, resetLogoutCounter } from '../actions/index';
import { buildSearchParams } from '../utils/react-table-search-params';

class UsersTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(UsersTable.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  constructor(props) {
    super(props);

    this.state = {
      loading: true,
    };

    // flag of someone is typing
    this.filtering = false;

    this.onFilteredChange = this.onFilteredChange.bind(this);
    this.fetchStrategy = this.fetchStrategy.bind(this);

    this.fetchData = this.fetchData.bind(this);
    this.fetchDataWithDebounce = _.debounce(this.fetchData, 500);
  }

  componentDidMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
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
        <div className="actions-buttons-container">
          <Link
            to={`/users/${cell.value}`}
            type="button"
            className="btn btn-primary margin-right-sm"
            title="Edit"
          >
            <span className="fa fa-edit" />
            <span className="hide-min-r-small-min next-button-text">Edit</span>
          </Link>
        </div>
      ),
      filterable: false,
      sortable: false,
    },
    {
      Header: 'Username',
      accessor: 'username',
    }, {
      Header: 'Email',
      accessor: 'email',
    }, {
      Header: 'Name',
      accessor: 'name',
    }, {
      Header: 'Role',
      accessor: 'roles[0].name',
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
    let index = _.findIndex(filters, ['id', 'roles[0].name']);
    if (index !== -1) {
      filters[index].id = 'role';
    }
    const sorting = tableState.sorted.map(a => ({ ...a }));
    index = _.findIndex(sorting, ['id', 'roles[0].name']);
    if (index !== -1) {
      sorting[index].id = 'role';
    }
    this.setState({ loading: true });
    this.props.fetchUsers(buildSearchParams(
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
            data={this.props.usersList}
            columns={UsersTable.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable
            manual
            filterable
            data={this.props.usersList}
            pages={this.props.userListPages}
            columns={UsersTable.getTableColumns()}
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
    usersList: state.tablesReducer.usersList,
    userListPages: state.tablesReducer.userListPages,
  };
}

export default withRouter(connect(mapStateToProps, {
  fetchUsers, resetLogoutCounter,
})(UsersTable));

UsersTable.propTypes = {
  fetchUsers: PropTypes.func.isRequired,
  usersList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  userListPages: PropTypes.number.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
