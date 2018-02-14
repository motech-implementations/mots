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
import { fetchCommunities } from '../actions/index';

const COLUMNS = [
  {
    Header: 'Name',
    accessor: 'name',
  }, {
    Header: 'Parent Facility',
    accessor: 'parent',
  },
];

class CommunitiesTable extends Component {
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
            data={this.props.communitiesList}
            columns={CommunitiesTable.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable filterable data={this.props.communitiesList} columns={COLUMNS} />
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    communitiesList: state.tablesReducer.communitiesList,
  };
}

export default connect(mapStateToProps, { fetchFacilities: fetchCommunities })(CommunitiesTable);

CommunitiesTable.propTypes = {
  fetchFacilities: PropTypes.func.isRequired,
  communitiesList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
