import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { toast } from 'react-toastify';
import { initialize } from 'redux-form';

import GroupForm, { GROUP_FORM_NAME } from './group-form';
import { hasAuthority, GROUP_WRITE_AUTHORITY } from '../utils/authorization';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';

class GroupEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      groupValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(GROUP_WRITE_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchGroup();
  }

  onSubmitCancel() {
    this.props.history.push('/groups');
  }

  onSubmit(groupValues) {
    this.setState({ showConfirmModal: true, groupValues });
  }

  onSubmitModal() {
    apiClient.put(`api/group/${this.state.groupValues.id}`, this.state.groupValues)
      .then(() => {
        toast.success('Group has been updated');
        this.props.history.push('/groups');
      });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchGroup() {
    const url = `/api/group/${this.props.match.params.groupId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          this.props.initialize(GROUP_FORM_NAME, response.data);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Group</h1>
        <GroupForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Group?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { initialize })(GroupEdit);

GroupEdit.propTypes = {
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      groupId: PropTypes.string,
    }),
  }).isRequired,
};
