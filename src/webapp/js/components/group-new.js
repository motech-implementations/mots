import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { toast } from 'react-toastify';
import { withRouter } from 'react-router-dom';

import GroupForm from './group-form';
import { hasAuthority, GROUP_WRITE_AUTHORITY } from '../utils/authorization';
import apiClient from '../utils/api-client';

class GroupNew extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(GROUP_WRITE_AUTHORITY)) {
      this.props.history.push('/home');
    }
  }

  onSubmitCancel() {
    this.props.history.push('/groups');
  }

  onSubmit(values) {
    apiClient.post('api/group', values)
      .then(() => {
        toast.success('New group has been created');
        this.props.history.push('/groups');
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Group</h1>
        <GroupForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default withRouter(GroupNew);

GroupNew.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
