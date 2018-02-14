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
import { fetchDistricts } from '../actions/index';

const COLUMNS = [
  {
    Header: 'Name',
    accessor: 'name',
  },
];

class DistrictsTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(COLUMNS);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.props.fetchDistricts();
    }
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable
            data={this.props.districtsList}
            columns={DistrictsTable.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable filterable data={this.props.districtsList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    districtsList: state.tablesReducer.districtsList,
  };
}

export default connect(mapStateToProps, { fetchDistricts })(DistrictsTable);

DistrictsTable.propTypes = {
  fetchDistricts: PropTypes.func.isRequired,
  districtsList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
