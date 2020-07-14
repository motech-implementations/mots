import _ from 'lodash';
import React, { Component } from 'react';
import { Async } from 'react-select';
import PropTypes from 'prop-types';
import { ClimbingBoxLoader } from 'react-spinners';

import DatePicker from '../utils/date-picker';
import apiClient from '../utils/api-client';

class GroupAssignFrom extends Component {
  static fetchAvailableModules() {
    const url = '/api/modules/simple';

    return apiClient.get(url)
      .then((response) => {
        const availableModulesList = _.map(
          response.data,
          module => ({ value: module.id, label: module.name }),
        );
        return { options: availableModulesList };
      });
  }

  render() {
    const { notificationTime } = this.props;

    return (
      <div>
        <Async
          value={this.props.selectedModules}
          loadOptions={GroupAssignFrom.fetchAvailableModules}
          onChange={this.props.handleModuleChange}
          onFocus={this.props.resetLogoutCounter}
          disabled={this.props.disableModuleSelect}
          placeholder="Select Modules assignment"
          multi
          className="margin-bottom-md col-md-12"
          menuContainerStyle={{ zIndex: 5 }}
        />
        <div className="col-md-12 margin-top-xs margin-bottom-xs">
          <input
            id="delay-notification"
            type="checkbox"
            className="checkbox-inline"
            checked={this.props.delayNotification}
            onChange={this.props.handleDelayNotificationChange}
          />
          {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
          <label htmlFor="delay-notification" className="margin-left-sm margin-bottom-sm">
            Delay the notification
          </label>
        </div>
        {this.props.delayNotification
        && (
        <div className="col-md-12 margin-top-sm">
          {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
          <label htmlFor="notification-time">Notification date</label>
          <DatePicker
            id="notification-time"
            showTimeSelect
            value={notificationTime}
            onChange={this.props.handleNotificationTimeChange}
            minDate={new Date()}
          />
        </div>
        )}
        <form
          className="form-horizontal col-md-12"
          onSubmit={this.props.sendAssignedModules}
        >
          <button
            type="submit"
            className="btn btn-primary btn-block margin-x-md padding-x-sm"
            disabled={!this.props.validate() || this.props.assignInProgress}
          >
            <h4>Assign!</h4>
          </button>
          <div style={{ marginBottom: '200px' }}>
            <ClimbingBoxLoader
              color="#000000"
              loading={this.props.assignInProgress}
            />
          </div>
        </form>
      </div>
    );
  }
}

export default GroupAssignFrom;

GroupAssignFrom.propTypes = {
  resetLogoutCounter: PropTypes.func.isRequired,
  handleModuleChange: PropTypes.func.isRequired,
  handleDelayNotificationChange: PropTypes.func.isRequired,
  handleNotificationTimeChange: PropTypes.func.isRequired,
  sendAssignedModules: PropTypes.func.isRequired,
  validate: PropTypes.func.isRequired,
  selectedModules: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.shape({})),
    PropTypes.shape({}),
  ]),
  delayNotification: PropTypes.bool,
  notificationTime: PropTypes.string,
  disableModuleSelect: PropTypes.bool,
  assignInProgress: PropTypes.bool,
};

GroupAssignFrom.defaultProps = {
  selectedModules: null,
  delayNotification: false,
  notificationTime: null,
  disableModuleSelect: false,
  assignInProgress: false,
};
