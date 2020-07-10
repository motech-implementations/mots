import _ from 'lodash';
import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { Link, withRouter } from 'react-router-dom';

import 'react-table/react-table.css';

import MobileTable from './mobile-table';
import { hasAuthority, GROUP_READ_AUTHORITY, GROUP_WRITE_AUTHORITY } from '../utils/authorization';
import apiClient from '../utils/api-client';

class Groups extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(Groups.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  static getTableColumns = () => [
    {
      Header: 'Actions',
      minWidth: 50,
      accessor: 'id',
      Cell: cell => (
        <div className="actions-buttons-container">
          <Link
            to={`/groups/${cell.value}`}
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
      show: hasAuthority(GROUP_WRITE_AUTHORITY),
    },
    {
      Header: 'Name',
      accessor: 'name',
    }];

  constructor(props) {
    super(props);

    this.state = {
      groupsData: [],
    };
  }

  componentDidMount() {
    if (!hasAuthority(GROUP_READ_AUTHORITY)) {
      this.props.history.push('/home');
    }

    this.fetchData();
  }

  fetchData() {
    apiClient.get('/api/group')
      .then((response) => {
        if (response) {
          this.setState({ groupsData: response.data });
        }
      });
  }

  filterMethod = (filter, row) => (_.toString(row[filter.id]).toLowerCase()
    .includes(filter.value.trim().toLowerCase()));

  render() {
    return (
      <div>
        <h1>Groups</h1>
        <div>
          <div className="hide-min-r-small-min">
            <MobileTable
              data={this.state.groupsData}
              columns={Groups.prepareMobileColumns()}
            />
          </div>
          <div className="hide-max-r-xsmall-max">
            <ReactTable
              filterable
              data={this.state.groupsData}
              columns={Groups.getTableColumns()}
              defaultFilterMethod={this.filterMethod}
            />
          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(Groups);

Groups.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
