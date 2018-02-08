import React, { Component } from 'react';
import { ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Button from '../components/Button';

import ListItems from '../components/ListItems';
import { fetchIncharges } from '../actions/index';

const COLUMNS = [
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
  },
  {
    Header: 'Actions',
    minWidth: 50,
    accessor: 'id',
    Cell: () => (
      <Button
        onPress={Actions.home}
        iconName="pencil-square-o"
        iconColor="#FFF"
        buttonColor="#337ab7"
      >
        Edit
      </Button>
    ),
  },
];


class InchargeList extends Component {
  componentWillMount() {
    this.props.fetchIncharges();
  }

  render() {
    return (
      <ScrollView>
        <ListItems data={this.props.incharges} columns={COLUMNS} />
      </ScrollView>
    );
  }
}

function mapStateToProps(state) {
  return {
    incharges: state.tablesReducer.inchargesList,
  };
}

export default connect(mapStateToProps, { fetchIncharges })(InchargeList);

InchargeList.propTypes = {
  fetchIncharges: PropTypes.func.isRequired,
  incharges: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
};
