import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import 'react-datetime/css/react-datetime.css';

import HealthWorkersForm from './health-workers-form';
import apiClient from '../utils/api-client';
import { selectHealthWorker } from '../actions';
import { CHW_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';

class HealthWorkersNew extends Component {
  constructor(props) {
    super(props);

    this.state = {
      notSelectedChwIds: [],
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(CHW_WRITE_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.fetchNotSelectedChwIds();
    }
  }

  onSubmitCancel() {
    this.props.history.push('/chw/selected');
  }

  onSubmit(values) {
    this.props.selectHealthWorker(values, () => {
      Alert.success('CHW has been added');
      this.props.history.push('/chw/selected');
    });
  }

  fetchNotSelectedChwIds() {
    const url = '/api/chw/notSelected';

    apiClient.get(url)
      .then((response) => {
        const notSelectedChwIds = response.data;

        this.setState({ notSelectedChwIds });
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Community Health Worker</h1>
        <HealthWorkersForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          notSelectedChwIds={this.state.notSelectedChwIds}
          addChw
        />
      </div>
    );
  }
}

export default connect(null, { selectHealthWorker })(HealthWorkersNew);

HealthWorkersNew.propTypes = {
  selectHealthWorker: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
