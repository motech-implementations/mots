import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { initialize } from 'redux-form';

import InchargesForm, { INCHARGE_FORM_NAME } from './InchargesForm';
import { saveIncharge } from '../actions';
import { INCHARGE_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import formsStyles from '../styles/formsStyles';
import apiClient from '../utils/api-client';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { formHeader } = formsStyles;
const { lightThemeText } = commonStyles;

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
    Actions.modalConfirm({
      message: 'Are you sure to edit Incharge?',
      onConfirm: () => {
        this.setState({ loading: true });
        this.props.saveIncharge(values, result => this.onSubmitSuccess(result));
      },
    });
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'Incharge has been successfully edited',
        onClose: () => { Actions.incharges(); },
      });
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
      <View style={getContainerStyle()}>
        <ScrollView>
          <Text style={[formHeader, lightThemeText]}>Edit Incharge</Text>
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
