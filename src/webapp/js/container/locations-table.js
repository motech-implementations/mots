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
import { fetchLocationsOfType } from '../actions/index';

const COLUMNS = [
  {
    Header: 'Name',
    accessor: 'name',
  }, {
    Header: 'Parent District',
    accessor: 'parent',
  },
];

class LocationsTable extends Component {
  static prepareMobileColumns() {
    const mobileColumns = _.clone(COLUMNS);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.props.fetchLocationsOfType(this.props.locationType);
    }
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable
            data={this.props.locationsList}
            columns={LocationsTable.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable
            data={this.props.locationsList}
            columns={this.props.tableColumns}
            filterable
          />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    locationsList: state.tablesReducer.locationsList,
  };
}

export default connect(mapStateToProps, { fetchLocationsOfType })(LocationsTable);

LocationsTable.propTypes = {
  fetchLocationsOfType: PropTypes.func.isRequired,
  locationsList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  tableColumns: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  locationType: PropTypes.string.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
