import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { initialize } from 'redux-form';

import HealthWorkersForm, { CHW_FORM_NAME } from './HealthWorkersForm';
import { saveHealthWorker } from '../actions';
import { CHW_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import formsStyles from '../styles/formsStyles';
import apiClient from '../utils/api-client';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { formHeader } = formsStyles;
const { lightThemeText } = commonStyles;

class HealthWorkersEdit extends Component {
  constructor(props) {
    super(props);
    this.state = { loading: false };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    hasAuthority(CHW_WRITE_AUTHORITY).then((result) => {
      if (!result) {
        Actions.home();
      }
      this.fetchChw();
    });
  }

  onSubmitCancel() {
    this.setState({ loading: false });
    Actions.chws();
  }

  onSubmit(values) {
    Actions.modalConfirm({
      message: 'Are you sure to edit Community Health Worker?',
      onConfirm: () => {
        this.setState({ loading: true });
        this.props.saveHealthWorker(values, result => this.onSubmitSuccess(result));
      },
    });
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'Health Worker has been successfully edited',
        onClose: () => { Actions.chws(); },
      });
    }
  }

  fetchChw() {
    const url = `/api/chw/${this.props.chwId}`;
    apiClient.get(url)
      .then((response) => {
        this.props.initialize(CHW_FORM_NAME, response);
      });
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <ScrollView>
          <Text style={[formHeader, lightThemeText]}>Edit Community Health Worker</Text>
          <HealthWorkersForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
            isChwIdDisabled
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { saveHealthWorker, initialize })(HealthWorkersEdit);

HealthWorkersEdit.propTypes = {
  saveHealthWorker: PropTypes.func.isRequired,
  chwId: PropTypes.string.isRequired,
  initialize: PropTypes.func.isRequired,
};
