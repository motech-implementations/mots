import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import ReactTable from 'react-table';

import 'react-table/react-table.css';

import { fetchIncharges } from '../actions/index';
import MobileTable from '../components/mobile-table';

const COLUMNS = [
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
  }];

class InchargeTable extends Component {
  componentWillMount() {
    this.props.fetchIncharges();
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable data={this.props.inchargesList} rowIdAccessor="id" columns={COLUMNS} />
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
    inchargesList: state.tablesReducer.inchargesList,
  };
}

export default connect(mapStateToProps, { fetchIncharges })(InchargeTable);

InchargeTable.propTypes = {
  fetchIncharges: PropTypes.func.isRequired,
  inchargesList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
};
