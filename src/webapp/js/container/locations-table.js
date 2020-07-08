import React, { Component } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import {
  DISPLAY_FACILITIES_AUTHORITY, MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import { fetchLocationsOfType, resetLogoutCounter } from '../actions/index';
import { buildSearchParams } from '../utils/react-table-search-params';

class LocationsTable extends Component {
  static prepareMobileColumns(tableColumns) {
    const mobileColumns = _.clone(tableColumns);
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
  }

  constructor(props) {
    super(props);
    // flag of someone is typing
    this.filtering = false;

    this.onFilteredChange = this.onFilteredChange.bind(this);
    this.fetchStrategy = this.fetchStrategy.bind(this);

    this.fetchData = this.fetchData.bind(this);
    this.fetchDataWithDebounce = _.debounce(this.fetchData, 500);
  }

  componentDidMount() {
    if (!hasAuthority(
      DISPLAY_FACILITIES_AUTHORITY,
      MANAGE_FACILITIES_AUTHORITY,
      MANAGE_OWN_FACILITIES_AUTHORITY,
    )) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
  }

  onFilteredChange() {
    // when the filter changes, someone is typing
    this.filtering = true;
  }

  fetchStrategy(tableState) {
    // if someone is typing use debounce
    if (this.filtering) {
      return this.fetchDataWithDebounce(tableState);
    }
    // if not typing (f.ex. sorting) fetch data without debounce
    return this.fetchData(tableState);
  }

  fetchData(tableState) {
    // filtering can be reset
    this.filtering = false;

    this.setState({ loading: true });

    this.props.fetchLocationsOfType(this.props.locationType, buildSearchParams(
      tableState.filtered,
      tableState.sorted,
      tableState.page,
      tableState.pageSize,
    ))
      .then(() => {
        this.setState({ loading: false });
      });

    this.props.resetLogoutCounter();
  }

  render() {
    return (
      <div>
        <div className="hide-min-r-small-min">
          <MobileTable
            data={this.props.locationsList}
            columns={LocationsTable.prepareMobileColumns(this.props.tableColumns)}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable
            manual
            filterable
            data={this.props.locationsList}
            pages={this.props.locationListPages}
            columns={this.props.tableColumns}
            loading={this.state.loading}
            onFetchData={this.fetchStrategy}
            onFilteredChange={this.onFilteredChange}
          />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    locationsList: state.tablesReducer.locationsList,
    locationListPages: state.tablesReducer.locationListPages,
  };
}

export default withRouter(connect(mapStateToProps, {
  fetchLocationsOfType, resetLogoutCounter,
})(LocationsTable));

LocationsTable.propTypes = {
  fetchLocationsOfType: PropTypes.func.isRequired,
  locationListPages: PropTypes.number.isRequired,
  locationsList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  tableColumns: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  locationType: PropTypes.string.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
