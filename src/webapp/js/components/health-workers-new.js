import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import 'react-datetime/css/react-datetime.css';

import HealthWorkersForm from './health-workers-form';
import { createHealthWorker } from '../actions';

class HealthWorkersNew extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  onSubmitCancel() {
    this.props.history.push('/chw');
  }

  onSubmit(values) {
    this.props.createHealthWorker(values, () => {
      Alert.success('CHW has been added');
      this.props.history.push('/chw');
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header">Add Community Health Worker</h1>
        <HealthWorkersForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createHealthWorker })(HealthWorkersNew);

HealthWorkersNew.propTypes = {
  createHealthWorker: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
