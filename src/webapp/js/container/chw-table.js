import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import ReactTable from 'react-table';
import { Link } from 'react-router-dom';

import 'react-table/react-table.css';

import { fetchChws } from '../actions/index';
import MobileTable from '../components/mobile-table';

const COLUMNS = [
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
  },
  {
    Header: 'Actions',
    minWidth: 150,
    accessor: 'id',
    Cell: cell => (
      <div>
        <Link
          to={`/chw/${cell.value}`}
          type="button"
          className="btn btn-primary center-block"
        ><span className="glyphicon glyphicon-edit" /> Edit
        </Link>
        <div className="padding-x-xs" />
        <Link
          to={`/modules/assign/${cell.value}`}
          type="button"
          className="btn btn-success center-block"
        >
          <span className="glyphicon glyphicon-circle-arrow-right" /> Assign Module
        </Link>
      </div>
    ),
  }];

class ChwTable extends Component {
  componentWillMount() {
    this.props.fetchChws();
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable data={this.props.chwList} columns={COLUMNS} />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable data={this.props.chwList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    chwList: state.chwList,
  };
}

export default connect(mapStateToProps, { fetchChws })(ChwTable);

ChwTable.propTypes = {
  fetchChws: PropTypes.func.isRequired,
  chwList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
};
