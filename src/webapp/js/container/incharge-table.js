import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import ReactTable from 'react-table';
import { Link, withRouter } from 'react-router-dom';
import _ from 'lodash';

import 'react-table/react-table.css';

import { fetchIncharges, resetLogoutCounter } from '../actions/index';
import MobileTable from '../components/mobile-table';
import {
  hasAuthority, INCHARGE_READ_AUTHORITY,
  INCHARGE_WRITE_AUTHORITY,
} from '../utils/authorization';
import { buildSearchParams } from '../utils/react-table-search-params';

class InchargeTable extends Component {
  constructor() {
    super();
    // flag of someone is typing
    this.filtering = false;

    this.onFilteredChange = this.onFilteredChange.bind(this);
    this.fetchStrategy = this.fetchStrategy.bind(this);

    this.fetchData = this.fetchData.bind(this);
    this.fetchDataWithDebounce = _.debounce(this.fetchData, 500);
  }

  componentWillMount() {
    if (!hasAuthority(INCHARGE_READ_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
  }

  onFilteredChange() {
    // when the filter changes, someone is typing
    this.filtering = true;
  }

  getTableColumns = () => [
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
      filterable: false,
      sortable: false,
      show: this.props.selected && hasAuthority(INCHARGE_WRITE_AUTHORITY),
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
    }, {
      Header: 'Facility ID',
      accessor: 'facilityIdentifier',
    }, {
      Header: 'Chiefdom',
      accessor: 'chiefdomName',
    }, {
      Header: 'District',
      accessor: 'districtName',
    }];

  prepareMobileColumns() {
    const mobileColumns = _.clone(this.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
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

    this.props.fetchIncharges(buildSearchParams(
      tableState.filtered,
      tableState.sorted,
      tableState.page,
      tableState.pageSize,
    ), this.props.selected)
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
            data={this.props.inchargesList}
            columns={this.prepareMobileColumns()}
          />
        </div>
        <div className="hide-max-r-xsmall-max">
          <ReactTable
            manual
            filterable
            data={this.props.inchargesList}
            pages={this.props.inchargeListPages}
            columns={this.getTableColumns()}
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
    inchargesList: state.tablesReducer.inchargesList,
    inchargeListPages: state.tablesReducer.inchargeListPages,
  };
}

export default withRouter(connect(mapStateToProps, {
  fetchIncharges, resetLogoutCounter,
})(InchargeTable));

InchargeTable.propTypes = {
  fetchIncharges: PropTypes.func.isRequired,
  inchargeListPages: PropTypes.number.isRequired,
  inchargesList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  selected: PropTypes.bool,
  resetLogoutCounter: PropTypes.func.isRequired,
};

InchargeTable.defaultProps = {
  selected: true,
};
