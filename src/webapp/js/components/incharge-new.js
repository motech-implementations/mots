import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import InchargeForm from './incharge-form';
import { createIncharge } from '../actions';

class InchargeNew extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  onSubmitCancel() {
    this.props.history.push('/incharge');
  }

  onSubmit(values) {
    this.props.createIncharge(values, () => {
      Alert.success('Incharge has been added');
      this.props.history.push('/incharge');
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Incharge</h1>
        <InchargeForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createIncharge })(InchargeNew);

InchargeNew.propTypes = {
  createIncharge: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
