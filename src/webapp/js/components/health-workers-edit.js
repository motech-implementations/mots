import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import 'react-datetime/css/react-datetime.css';

import HealthWorkersForm, { CHW_FORM_NAME } from './health-workers-form';
import { saveHealthWorker } from '../actions';
import apiClient from '../utils/api-client';

class HealthWorkersEdit extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    this.fetchChw();
  }

  onSubmitCancel() {
    this.props.history.push('/chw');
  }

  onSubmit(values) {
    this.props.saveHealthWorker(values, () => {
      Alert.success('CHW has been saved');
      this.props.history.push('/chw');
    });
  }

  fetchChw() {
    const url = `/api/chw/${this.props.match.params.chwId}`;

    apiClient.get(url)
      .then((response) => {
        const chw = response.data;

        this.props.initialize(CHW_FORM_NAME, chw);
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Community Health Worker</h1>
        <HealthWorkersForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
        />
      </div>
    );
  }
}

export default connect(null, { saveHealthWorker, initialize })(HealthWorkersEdit);

HealthWorkersEdit.propTypes = {
  saveHealthWorker: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      chwId: PropTypes.string,
    }),
  }).isRequired,
  initialize: PropTypes.func.isRequired,
};
