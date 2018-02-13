import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { fetchUsers } from '../actions/index';

const COLUMNS = [
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
  },
];

class UsersTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(COLUMNS);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.props.fetchUsers();
    }
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable data={this.props.usersList} columns={UsersTable.prepareMobileColumns()} />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable data={this.props.usersList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    usersList: state.tablesReducer.usersList,
  };
}

export default connect(mapStateToProps, { fetchUsers })(UsersTable);

UsersTable.propTypes = {
  fetchUsers: PropTypes.func.isRequired,
  usersList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
