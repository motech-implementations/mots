import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import CommunityForm from './community-form';
import { createCommunity } from '../actions';
import {
  hasAuthority,
  CREATE_FACILITIES_AUTHORITY,
} from '../utils/authorization';

class CommunityNew extends Component {
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
    this.props.history.push('/');
  }

  onSubmit(values) {
    const valuesToSend = {
      name: values.name,
      facilityId: values.facilityId,
    };

    this.props.createCommunity(valuesToSend, () => {
      Alert.success('Community has been added');
      this.props.history.push('/locations');
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Community</h1>
        <CommunityForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createCommunity })(CommunityNew);

CommunityNew.propTypes = {
  createCommunity: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
