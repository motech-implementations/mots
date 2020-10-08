import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { toast } from 'react-toastify';

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

  componentDidMount() {
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
      sectorId: values.sectorId,
      facilityType: values.facilityType,
      inchargeFullName: values.inchargeFullName,
      inchargePhone: values.inchargePhone,
      inchargeEmail: values.inchargeEmail,
    };

    this.props.createFacility(valuesToSend, () => {
      toast.success('Facility has been added');
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
