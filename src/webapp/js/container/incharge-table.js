import React, { Component } from 'react';
import { initialize } from 'redux-form';
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
  componentWillMount() {
    if (!hasAuthority(INCHARGE_READ_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.setState({ loading: true });
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
    }];

  prepareMobileColumns() {
    const mobileColumns = _.clone(this.getTableColumns());
    mobileColumns.push(mobileColumns.shift());
    return mobileColumns;
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
            columns={this.getTableColumns()}
            loading={this.state.loading}
            pages={this.props.inchargeListPages}
            onFetchData={(state) => {
              this.setState({ loading: true });
              this.props.fetchIncharges(buildSearchParams(
                  state.filtered,
                  state.sorted,
                  state.page,
                  state.pageSize,
              ), this.props.selected)
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
    inchargesList: state.tablesReducer.inchargesList,
    inchargeListPages: state.tablesReducer.inchargeListPages,
  };
}

export default withRouter(connect(mapStateToProps, {
  fetchIncharges, initialize, resetLogoutCounter,
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
