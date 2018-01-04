import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import 'react-datetime/css/react-datetime.css';

import HealthWorkersForm, { CHW_FORM_NAME } from './health-workers-form';
import { saveHealthWorker } from '../actions';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';

class HealthWorkersEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      values: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
  }

  componentWillMount() {
    this.fetchChw();
  }

  onSubmitCancel() {
    this.props.history.push('/chw');
  }

  onSubmit(values) {
    this.setState({ showConfirmModal: true, values });
  }

  onSubmitModal() {
    this.props.saveHealthWorker(this.state.values, () => {
      Alert.success('CHW has been saved');
      this.props.history.push('/chw');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
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
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Community Health Worker?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
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
