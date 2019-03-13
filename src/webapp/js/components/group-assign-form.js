import _ from 'lodash';
import React, { Component } from 'react';
import { Async } from 'react-select';
import PropTypes from 'prop-types';
import DateTime from 'react-datetime';

import 'react-datetime/css/react-datetime.css';

import { getDefaultNotificationDate } from '../utils/form-utils';
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
        <div className="col-md-6 margin-bottom-md">
          <label htmlFor="start-date">Start date</label>
          <div className="input-group">
            <span className="input-group-addon"><i className="fa fa-calendar" /></span>
            <DateTime
              dateFormat="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              inputProps={{ disabled: !this.props.selectedModules }}
              value={this.props.startDate}
              onChange={this.props.handleStartDateChange}
              id="start-date"
            />
          </div>
        </div>
        <div className="col-md-6 margin-bottom-md">
          <label htmlFor="end-date">End date</label>
          <div className="input-group">
            <span className="input-group-addon"><i className="fa fa-calendar" /></span>
            <DateTime
              dateFormat="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              inputProps={{ disabled: !this.props.selectedModules }}
              value={this.props.endDate}
              onChange={this.props.handleEndDateChange}
              id="end-date"
            />
          </div>
        </div>
        <div className="col-md-12 margin-top-xs margin-bottom-xs">
          <input
            id="delay-notification"
            type="checkbox"
            className="checkbox-inline"
            checked={this.props.delayNotification}
            onChange={this.props.handleDelayNotificationChange}
          />
          <label htmlFor="delay-notification" className="margin-left-sm margin-bottom-sm">
            Delay the notification
          </label>
        </div>
        {this.props.delayNotification &&
        <div className="col-md-12 margin-top-sm">
          <label htmlFor="notification-time">Notification date</label>
          <div className="input-group">
            <span className="input-group-addon">
              <i className="fa fa-calendar" />
            </span>
            <DateTime
              dateFormat="YYYY-MM-DD"
              timeFormat="HH:mm"
              closeOnSelect
              onChange={this.props.handleNotificationTimeChange}
              id="notification-time"
              defaultValue={getDefaultNotificationDate()}
              isValidDate={current => current.isSameOrAfter(new Date(), 'day')}
            />
          </div>
        </div>
        }
        <form
          className="form-horizontal col-md-12"
          onSubmit={this.props.sendAssignedModules}
        >
          <button
            type="submit"
            className="btn btn-primary btn-block margin-x-md padding-x-sm"
            style={{ marginBottom: '200px' }}
            disabled={!this.props.validate()}
          >
            <h4>Assign!</h4>
          </button>
        </form>
      </div>
    );
  }
}

export default GroupAssignFrom;

GroupAssignFrom.propTypes = {
  resetLogoutCounter: PropTypes.func.isRequired,
  handleModuleChange: PropTypes.func.isRequired,
  handleStartDateChange: PropTypes.func.isRequired,
  handleEndDateChange: PropTypes.func.isRequired,
  handleDelayNotificationChange: PropTypes.func.isRequired,
  handleNotificationTimeChange: PropTypes.func.isRequired,
  sendAssignedModules: PropTypes.func.isRequired,
  validate: PropTypes.func.isRequired,
  selectedModules: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.shape({})),
    PropTypes.shape({}),
  ]),
  startDate: PropTypes.oneOfType([
    PropTypes.shape({}),
    PropTypes.string,
  ]),
  endDate: PropTypes.oneOfType([
    PropTypes.shape({}),
    PropTypes.string,
  ]),
  delayNotification: PropTypes.bool,
  disableModuleSelect: PropTypes.bool,
};

GroupAssignFrom.defaultProps = {
  selectedModules: null,
  startDate: '',
  endDate: '',
  delayNotification: false,
  disableModuleSelect: false,
};

