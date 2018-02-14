import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import {
  hasAuthority, MANAGE_FACILITIES_AUTHORITY,
} from '../utils/authorization';
import { fetchFacilities } from '../actions/index';

const COLUMNS = [
  {
    Header: 'Facility ID',
    accessor: 'facilityId',
  }, {
    Header: 'Name',
    accessor: 'name',
  }, {
    Header: 'Facility Type',
    accessor: 'facilityType',
  }, {
    Header: 'Incharge name',
    accessor: 'inchargeFullName',
  }, {
    Header: 'Parent Chiefdom',
    accessor: 'parent',
  },
];

class FacilitiesTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(COLUMNS);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.props.fetchFacilities();
    }
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable
            data={this.props.facilitiesList}
            columns={FacilitiesTable.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable filterable data={this.props.facilitiesList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    facilitiesList: state.tablesReducer.facilitiesList,
  };
}

export default connect(mapStateToProps, { fetchFacilities })(FacilitiesTable);

FacilitiesTable.propTypes = {
  fetchFacilities: PropTypes.func.isRequired,
  facilitiesList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
