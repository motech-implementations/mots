import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';

import InchargesForm from './InchargesForm';
import { selectIncharge } from '../actions';
import { INCHARGE_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import formsStyles from '../styles/formsStyles';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { formHeader } = formsStyles;
const { lightThemeText } = commonStyles;

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
    this.props.selectIncharge(values, result => this.onSubmitSuccess(result));
  }

  onSubmitSuccess(result) {
    this.setState({ loading: false });
    if (result) {
      Actions.modalSuccess({
        message: 'New Incharge has been created',
        onClose: () => { Actions.incharges(); },
      });
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <ScrollView>
          <Text style={[formHeader, lightThemeText]}>Add Incharge</Text>
          <InchargesForm
            loading={this.state.loading}
            onSubmit={this.onSubmit}
            onSubmitCancel={this.onSubmitCancel}
            addIncharge
          />
        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { selectIncharge })(InchargesNew);

InchargesNew.propTypes = {
  selectIncharge: PropTypes.func.isRequired,
};
