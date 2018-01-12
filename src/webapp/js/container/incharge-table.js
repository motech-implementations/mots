import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import ReactTable from 'react-table';
import { Link } from 'react-router-dom';

import 'react-table/react-table.css';

import { fetchIncharges } from '../actions/index';
import MobileTable from '../components/mobile-table';

const COLUMNS = [
  {
    Header: 'Actions',
    minWidth: 50,
    accessor: 'id',
    Cell: cell => (
      <div className="actions-buttons-container">
        <Link
          to={`/incharge/${cell.value}`}
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
    Header: 'First name',
    accessor: 'firstName',
  }, {
    Header: 'Surname',
    accessor: 'secondName',
  }, {
    Header: 'Other name',
    accessor: 'otherName',
  }, {
    Header: 'Phone number',
    accessor: 'phoneNumber',
  }, {
    Header: 'Email',
    accessor: 'email',
  }, {
    Header: 'Facility',
    accessor: 'facilityName',
  }];

class InchargeTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(COLUMNS);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    this.props.fetchIncharges();
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable data={this.props.inchargesList} columns={InchargeTable.prepareMobileColumns()} />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable data={this.props.inchargesList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    inchargesList: state.tablesReducer.get('inchargesList'),
  };
}

export default connect(mapStateToProps, { fetchIncharges })(InchargeTable);

InchargeTable.propTypes = {
  fetchIncharges: PropTypes.func.isRequired,
  inchargesList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
};
