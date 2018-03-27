import React, { Component } from 'react';
import { initialize } from 'redux-form';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import MobileTable from '../components/mobile-table';
import {
  DISPLAY_FACILITIES_AUTHORITY,
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

  componentWillMount() {
    if (!hasAuthority(DISPLAY_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
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
            columns={this.props.tableColumns}
            loading={this.state.loading}
            pages={this.props.locationListPages}
            onFetchData={(state) => {
              this.setState({ loading: true });
              this.props.fetchLocationsOfType(this.props.locationType, buildSearchParams(
                  state.filtered,
                  state.sorted,
                  state.page,
                  state.pageSize,
              ))
              .then(() => {
                this.setState({ loading: false });
              });
              this.props.resetLogoutCounter();
            }}
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
  fetchLocationsOfType, initialize, resetLogoutCounter,
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
