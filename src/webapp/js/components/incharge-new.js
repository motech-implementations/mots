import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import InchargeForm from './incharge-form';
import { selectIncharge } from '../actions';
import { hasAuthority, INCHARGE_WRITE_AUTHORITY } from '../utils/authorization';

class InchargeNew extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(INCHARGE_WRITE_AUTHORITY)) {
      this.props.history.push('/home');
    }
  }

  onSubmitCancel() {
    this.props.history.push('/incharge/selected');
  }

  onSubmit(values) {
    this.props.selectIncharge(values, () => {
      Alert.success('Incharge has been added');
      this.props.history.push('/incharge/selected');
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Incharge</h1>
        <InchargeForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} addIncharge />
      </div>
    );
  }
}

export default connect(null, { selectIncharge })(InchargeNew);

InchargeNew.propTypes = {
  selectIncharge: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
