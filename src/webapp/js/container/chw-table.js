import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import 'react-table/react-table.css';

import { fetchChws } from '../actions/index';
import ReactTable from 'react-table';

class ChwTable extends Component {

  constructor(props){
    super(props);
    this.props.fetchChws();
  }

  render() {

    const columns = [{
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

    return <ReactTable data={ this.props.chwList } columns={ columns } />
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ fetchChws }, dispatch)
}

function mapStateToProps(state) {
  return {
    chwList: state.chwList
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(ChwTable);