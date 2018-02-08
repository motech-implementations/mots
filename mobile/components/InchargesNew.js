import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView, Alert } from 'react-native';
import { Actions } from 'react-native-router-flux';

import InchargesForm from './InchargesForm';
import { createIncharge } from '../actions';
import { INCHARGE_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import listsStyles from '../styles/listsStyles';
import formsStyles from '../styles/formsStyles';

const { container } = listsStyles;
const { formHeader } = formsStyles;

class InchargesNew extends Component {
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
    });
  }

  onSubmitCancel() {
    this.setState({ loading: false });
    Actions.home();
  }

  onSubmit(values) {
    this.setState({ loading: true });
    this.props.createIncharge(values, result => this.onSubmitSuccess(result));
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Alert.alert(
        'Success!',
        'New Incharge has been created',
        [{ text: 'OK', onPress: () => Actions.incharges() }],
        { cancelable: false },
      );
    }
  }

  render() {
    return (
      <View style={[container, { marginBottom: 0 }]}>
        <ScrollView>
          <Text style={formHeader}>Add Incharge</Text>
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

export default connect(null, { createIncharge })(InchargesNew);

InchargesNew.propTypes = {
  createIncharge: PropTypes.func.isRequired,
};
