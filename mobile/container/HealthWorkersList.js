import React, { Component } from 'react';
import { ScrollView, Text, TouchableOpacity, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Icon from 'react-native-vector-icons/FontAwesome';

import ListItems from '../components/ListItems';
import { fetchChws, signinUser } from '../actions/index';

import styles from '../styles/listsStyles';

const COLUMNS = [
  {
    Header: 'ID',
    accessor: 'chwId',
  }, {
    Header: 'First name',
    accessor: 'firstName',
  }, {
    Header: 'Surname',
    accessor: 'secondName',
  }, {
    Header: 'Other name',
    accessor: 'otherName',
  }, {
    Header: 'DOB',
    accessor: 'dateOfBirth',
  }, {
    Header: 'Gender',
    accessor: 'gender',
  }, {
    Header: 'Education level',
    accessor: 'educationLevel',
  }, {
    Header: 'Literacy',
    accessor: 'literacy',
  }, {
    Header: 'Community',
    accessor: 'communityName',
  }, {
    Header: 'Preferred language',
    accessor: 'preferredLanguage',
  }, {
    Header: 'Phone number',
    accessor: 'phoneNumber',
  },
  {
    Header: 'Actions',
    minWidth: 70,
    accessor: 'id',
    Cell: () => (
      <View style={styles.buttonContainer}>
        <TouchableOpacity
          onPress={Actions.home}
          style={styles.actionButton}
        >
          <Icon name="pencil-square-o" color="#FFF" size={16} />
          <Text style={styles.buttonLabel}>Edit</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={Actions.home}
          style={[styles.actionButton, styles.actionButtonAssign]}
        >
          <Icon name="arrow-circle-o-right" color="#FFF" size={16} />
          <Text style={styles.buttonLabel}>Assign Modules</Text>
        </TouchableOpacity>
      </View>
    ),
  },
];


class HealthWorkersList extends Component {
  componentWillMount() {
    this.props.signinUser({
      username: 'admin',
      password: 'password',
    }, () => this.props.fetchChws());
  }

  render() {
    return (
      <ScrollView style={{ marginBottom: 40 }}>
        <ListItems data={this.props.chwList} columns={COLUMNS} />
      </ScrollView>
    );
  }
}

function mapStateToProps(state) {
  return {
    chwList: state.tablesReducer.chwList,
  };
}

export default connect(mapStateToProps, { fetchChws, signinUser })(HealthWorkersList);

HealthWorkersList.propTypes = {
  fetchChws: PropTypes.func.isRequired,
  signinUser: PropTypes.func.isRequired,
  chwList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
};
