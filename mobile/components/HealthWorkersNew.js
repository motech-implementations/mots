import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';

import HealthWorkersForm from './HealthWorkersForm';
import { createHealthWorker } from '../actions';
import { CHW_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import formsStyles from '../styles/formsStyles';
import getContainerStyle from '../utils/styleUtils';

const { formHeader } = formsStyles;

class HealthWorkersNew extends Component {
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
    });
  }

  onSubmitCancel() {
    this.setState();
    Actions.home();
  }

  onSubmit(values) {
    this.setState({ loading: true });
    this.props.createHealthWorker(values, result => this.onSubmitSuccess(result));
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'Incharge has been successfully edited',
        onClose: () => { Actions.chws(); },
      });
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <ScrollView>
          <Text style={formHeader}>Add Community Health Worker</Text>
          <HealthWorkersForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { createHealthWorker })(HealthWorkersNew);

HealthWorkersNew.propTypes = {
  createHealthWorker: PropTypes.func.isRequired,
};
