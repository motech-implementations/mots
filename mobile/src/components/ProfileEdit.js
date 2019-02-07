import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { initialize } from 'redux-form';

import ProfileForm, { PROFILE_FORM_NAME } from './ProfileForm';
import { saveProfile } from '../actions';
import formsStyles from '../styles/formsStyles';
import getContainerStyle from '../utils/styleUtils';
import apiClient from '../utils/api-client';
import commonStyles from '../styles/commonStyles';

const { formHeader } = formsStyles;
const { lightThemeText } = commonStyles;

class ProfileEdit extends Component {
  constructor(props) {
    super(props);
    this.state = { loading: false };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    this.fetchProfile();
  }

  onSubmitCancel() {
    this.setState({ loading: false });
    Actions.home();
  }

  onSubmit(values) {
    Actions.modalConfirm({
      message: 'Are you sure to edit Profile?',
      onConfirm: () => {
        this.setState({ loading: true });
        this.props.saveProfile(values, result => this.onSubmitSuccess(result));
      },
    });
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'Profile has been updated',
        onClose: () => { Actions.home(); },
      });
    }
  }

  fetchProfile() {
    const url = '/api/user/profile';
    apiClient.get(url)
      .then((response) => {
        if (response) {
          this.props.initialize(PROFILE_FORM_NAME, response);
        }
      });
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <ScrollView alwaysBounceVertical={false}>
          <Text style={[formHeader, lightThemeText]}>Edit Profile</Text>
          <ProfileForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
            isPasswordRequired={false}
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { saveProfile, initialize })(ProfileEdit);

ProfileEdit.propTypes = {
  saveProfile: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
};
