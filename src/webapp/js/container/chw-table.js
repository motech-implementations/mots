import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import ReactTable from 'react-table';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import { fetchChws } from '../actions/index';
import MobileTable from '../components/mobile-table';
import { hasAuthority, CHW_READ_AUTHORITY } from '../utils/authorization';

const COLUMNS = [
  {
    Header: 'Actions',
    minWidth: 70,
    accessor: 'id',
    Cell: cell => (
      <div className="actions-buttons-container">
        <Link
          to={`/chw/${cell.value}`}
          type="button"
          className="btn btn-primary margin-right-sm"
          title="Edit"
        >
          <span className="glyphicon glyphicon-edit" />
          <span className="hide-min-r-small-min next-button-text">Edit</span>
        </Link>
        <Link
          to={`/modules/assign/${cell.value}`}
          type="button"
          className="btn btn-success"
          title="Assign Module"
        >
          <span className="glyphicon glyphicon-circle-arrow-right" />
          <span className="hide-min-r-small-min next-button-text">Assign Module</span>
        </Link>
      </div>
    ),
    filterable: false,
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
    Header: 'DOB',
    accessor: 'dateOfBirth',
  }, {
    Header: 'Gender',
    accessor: 'gender',
  }, {
    Header: 'Education level',
    accessor: 'educationLevel',
  }, {
    Header: 'Literacy',
    accessor: 'literacy',
  }, {
    Header: 'Community',
    accessor: 'communityName',
  }, {
    Header: 'Preferred language',
    accessor: 'preferredLanguage',
  }, {
    Header: 'Phone number',
    accessor: 'phoneNumber',
  }];

class ChwTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(COLUMNS);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    if (!hasAuthority(CHW_READ_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.props.fetchChws();
    }
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable data={this.props.chwList} columns={ChwTable.prepareMobileColumns()} />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable filterable data={this.props.chwList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    chwList: state.tablesReducer.chwList,
  };
}

export default connect(mapStateToProps, { fetchChws })(ChwTable);

ChwTable.propTypes = {
  fetchChws: PropTypes.func.isRequired,
  chwList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
