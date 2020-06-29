import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import SectorForm from './sector-form';
import { createSector } from '../actions';
import {
  hasAuthority,
  CREATE_FACILITIES_AUTHORITY,
} from '../utils/authorization';

class SectorNew extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(CREATE_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    }
  }

  onSubmitCancel() {
    this.props.history.push('/locations/2');
  }

  onSubmit(values) {
    const valuesToSend = {
      name: values.name,
      districtId: values.districtId,
    };

    this.props.createSector(valuesToSend, () => {
      Alert.success('Chiefdom has been added');
      this.props.history.push({
        pathname: '/locations/2',
      });
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Chiefdom</h1>
        <SectorForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createSector })(SectorNew);

SectorNew.propTypes = {
  createSector: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
