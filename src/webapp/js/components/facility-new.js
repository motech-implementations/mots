import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import FacilityForm from './facility-form';
import { createFacility } from '../actions';
import {
  hasAuthority,
  CREATE_FACILITIES_AUTHORITY,
} from '../utils/authorization';

class FacilityNew extends Component {
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
    this.props.history.push('/locations/1');
  }

  onSubmit(values) {
    const valuesToSend = {
      name: values.name,
      chiefdomId: values.chiefdomId,
      facilityType: values.facilityType,
      facilityId: values.facilityId,
    };

    this.props.createFacility(valuesToSend, () => {
      Alert.success('Facility has been added');
      this.props.history.push({
        pathname: '/locations/1',
      });
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Facility</h1>
        <FacilityForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createFacility })(FacilityNew);

FacilityNew.propTypes = {
  createFacility: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
