import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import ReactTable from 'react-table';

import 'react-table/react-table.css';

import { fetchChws } from '../actions/index';

const COLUMNS = [{
  Header: 'ID',
  accessor: 'chwId'
},{
  Header: 'First name',
  accessor: 'firstName'
}, {
  Header: 'Second name',
  accessor: 'secondName'
}, {
  Header: 'Other name',
  accessor: 'otherName'
}, {
  Header: 'DOB',
  accessor: 'dateOfBirth'
}, {
  Header: 'Gender',
  accessor: 'gender'
}, {
  Header: 'Education level',
  accessor: 'educationLevel'
}, {
  Header: 'Literacy',
  accessor: 'literacy'
}, {
  Header: 'Community',
  accessor: 'communityName'
}, {
  Header: 'Preferred language',
  accessor: 'preferredLanguage'
}, {
  Header: 'Phone number',
  accessor: 'phoneNumber'
}];

class ChwTable extends Component {

  componentWillMount() {
    this.props.fetchChws();
  }

  render() {
    return <ReactTable data={ this.props.chwList } columns={ COLUMNS } />
  }
}

function mapStateToProps(state) {
  return {
    chwList: state.chwList
  };
}

export default connect(mapStateToProps, { fetchChws })(ChwTable);