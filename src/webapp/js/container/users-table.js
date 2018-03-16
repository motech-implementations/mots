import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { fetchUsers } from '../actions/index';
import { buildSearchParams } from '../utils/react-table-search-params';


class UsersTable extends Component {
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
            <span className="glyphicon glyphicon-edit" />
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

  static prepareMobileColumns() {
    const mobileColumns = _.clone(UsersTable.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
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
            columns={UsersTable.getTableColumns()}
            loading={this.state.loading}
            pages={this.props.userListPages}
            onFetchData={(state) => {
              const filters = state.filtered.map(a => ({ ...a }));
              const index = _.findIndex(filters, ['id', 'roles[0].name']);
              if (index !== -1) {
                filters[index].id = 'role';
              }
              this.setState({ loading: true });
              this.props.fetchUsers(buildSearchParams(
                  filters,
                  state.sorted,
                  state.page,
                  state.pageSize,
              ))
              .then(() => {
                this.setState({ loading: false });
              });
            }}
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

export default withRouter(connect(mapStateToProps, { fetchUsers })(UsersTable));

UsersTable.propTypes = {
  fetchUsers: PropTypes.func.isRequired,
  usersList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  userListPages: PropTypes.number.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
