import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView, Alert } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { initialize } from 'redux-form';

import InchargesForm, { INCHARGE_FORM_NAME } from './InchargesForm';
import { saveIncharge } from '../actions';
import { INCHARGE_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import listsStyles from '../styles/listsStyles';
import formsStyles from '../styles/formsStyles';
import apiClient from '../utils/api-client';

const { container } = listsStyles;
const { formHeader } = formsStyles;

class InchargesEdit extends Component {
  constructor(props) {
    super(props);
    this.state = { loading: false };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    hasAuthority(INCHARGE_WRITE_AUTHORITY).then((result) => {
      if (!result) {
        Actions.home();
      }
      this.fetchIncharge();
    });
  }

  onSubmitCancel() {
    this.setState({ loading: false });
    Actions.incharges();
  }

  onSubmit(values) {
    Alert.alert(
      '',
      'Are you sure to edit Incharge?',
      [{
        text: 'Confirm',
        onPress: () => {
          this.setState({ loading: true });
          this.props.saveIncharge(values, result => this.onSubmitSuccess(result));
        },
      },
      {
        text: 'Cancel',
        onPress: () => {},
      }],
      { cancelable: false },
    );
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Alert.alert(
        'Success!',
        'Incharge has been successfully edited',
        [{ text: 'OK', onPress: () => Actions.incharges() }],
        { cancelable: false },
      );
    }
  }

  fetchIncharge() {
    const url = `/api/incharge/${this.props.inchargeId}`;
    apiClient.get(url)
      .then((response) => {
        this.props.initialize(INCHARGE_FORM_NAME, response);
      });
  }

  render() {
    return (
      <View style={[container, { marginBottom: 0 }]}>
        <ScrollView>
          <Text style={formHeader}>Edit Incharge</Text>
          <InchargesForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { saveIncharge, initialize })(InchargesEdit);

InchargesEdit.propTypes = {
  saveIncharge: PropTypes.func.isRequired,
  inchargeId: PropTypes.string.isRequired,
  initialize: PropTypes.func.isRequired,
};
