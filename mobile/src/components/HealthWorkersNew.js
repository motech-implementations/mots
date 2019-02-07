import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';

import HealthWorkersForm from './HealthWorkersForm';
import { selectHealthWorker } from '../actions';
import { CHW_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import formsStyles from '../styles/formsStyles';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';
import apiClient from '../utils/api-client';

const { formHeader } = formsStyles;
const { lightThemeText } = commonStyles;

class HealthWorkersNew extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      notSelectedChwIds: [],
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    hasAuthority(CHW_WRITE_AUTHORITY).then((result) => {
      if (!result) {
        Actions.home();
      }
    });
    this.fetchNotSelectedChwIds();
  }

  onSubmitCancel() {
    this.setState();
    Actions.home();
  }

  onSubmit(values) {
    this.setState({ loading: true });
    this.props.selectHealthWorker(values, result => this.onSubmitSuccess(result));
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'Health Worker has been added',
        onClose: () => { Actions.chws(); },
      });
    }
  }

  fetchNotSelectedChwIds() {
    const url = '/api/chw/notSelected';

    apiClient.get(url)
      .then((response) => {
        this.setState({ notSelectedChwIds: response });
      });
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <ScrollView alwaysBounceVertical={false}>
          <Text style={[formHeader, lightThemeText]}>Add Community Health Worker</Text>
          <HealthWorkersForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
            notSelectedChwIds={this.state.notSelectedChwIds}
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { selectHealthWorker })(HealthWorkersNew);

HealthWorkersNew.propTypes = {
  selectHealthWorker: PropTypes.func.isRequired,
};
