import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';

import UserForm from './UserForm';
import { createUser } from '../actions';
import {
  MANAGE_USERS_AUTHORITY,
  MANAGE_INCHARGE_USERS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import formsStyles from '../styles/formsStyles';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { formHeader } = formsStyles;
const { lightThemeText } = commonStyles;

class UserNew extends Component {
  constructor(props) {
    super(props);
    this.state = { loading: false };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_INCHARGE_USERS_AUTHORITY).then((result) => {
      if (!result) {
        Actions.home();
      }
    });
  }

  onSubmitCancel() {
    this.setState({ loading: false });
    Actions.users();
  }

  onSubmit(values) {
    const valuesToSend = values;
    valuesToSend.roles = [{ id: values.roleId }];
    this.setState({ loading: true });
    this.props.createUser(valuesToSend, result => this.onSubmitSuccess(result));
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'New User has been created',
        onClose: () => { Actions.users(); },
      });
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <ScrollView alwaysBounceVertical={false}>
          <Text style={[formHeader, lightThemeText]}>Add User</Text>
          <UserForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { createUser })(UserNew);

UserNew.propTypes = {
  createUser: PropTypes.func.isRequired,
};
