import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import 'react-datetime/css/react-datetime.css';

import InchargeForm, { INCHARGE_FORM_NAME } from './incharge-form';
import { saveIncharge } from '../actions';
import apiClient from '../utils/api-client';

class InchargeEdit extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    this.fetchIncharge();
  }

  onSubmitCancel() {
    this.props.history.push('/incharge');
  }

  onSubmit(values) {
    this.props.saveIncharge(values, () => {
      Alert.success('Incharge has been saved');
      this.props.history.push('/incharge');
    });
  }

  fetchIncharge() {
    const url = `/api/incharge/${this.props.match.params.inchargeId}`;

    apiClient.get(url)
      .then((response) => {
        const incharge = response.data;

        this.props.initialize(INCHARGE_FORM_NAME, incharge, 'wtf');
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Incharge data</h1>
        <InchargeForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
        />
      </div>
    );
  }
}

export default connect(null, { saveIncharge, initialize })(InchargeEdit);

InchargeEdit.propTypes = {
  saveIncharge: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      inchargeId: PropTypes.string,
    }),
  }).isRequired,
  initialize: PropTypes.func.isRequired,
};
