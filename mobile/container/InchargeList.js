import React, { Component } from 'react';
import { ScrollView, Text, TouchableOpacity } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Icon from 'react-native-vector-icons/FontAwesome';

import ListItems from '../components/ListItems';
import { fetchIncharges, signinUser } from '../actions/index';

const styles = {
  actionButton: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: 60,
    borderRadius: 3,
    backgroundColor: '#337ab7',
    paddingHorizontal: 10,
    paddingVertical: 5,
  },
  buttonLabel: {
    marginLeft: 5,
    color: '#FFF',
  },
};

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
      <TouchableOpacity
        onPress={Actions.home}
        style={styles.actionButton}
      >
        <Icon name="pencil-square-o" color="#FFF" size={16} />
        <Text style={styles.buttonLabel}>Edit</Text>
      </TouchableOpacity>
    ),
  },
];


class InchargeList extends Component {
  componentWillMount() {
    this.props.signinUser({
      username: 'admin',
      password: 'password',
    }, () => this.props.fetchIncharges());
  }

  render() {
    return (
      <ScrollView style={{ marginBottom: 40 }}>
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

export default connect(mapStateToProps, { fetchIncharges, signinUser })(InchargeList);

InchargeList.propTypes = {
  fetchIncharges: PropTypes.func.isRequired,
  signinUser: PropTypes.func.isRequired,
  incharges: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
};
