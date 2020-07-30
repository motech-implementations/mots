import React from 'react';
import PropTypes from 'prop-types';
import { ClimbingBoxLoader } from 'react-spinners';

import Select from '../utils/select';
import DatePicker from '../utils/date-picker';

const GroupAssignFrom = props => (
  <div>
    <Select
      value={props.selectedModules}
      options={props.modulesOptions}
      onChange={props.handleModuleChange}
      onFocus={props.resetLogoutCounter}
      isDisabled={props.disableModuleSelect}
      placeholder="Select Modules assignment"
      isMulti
      className="margin-bottom-md col-md-12"
    />
    <div className="col-md-12 margin-top-xs margin-bottom-xs">
      <input
        id="delay-notification"
        type="checkbox"
        checked={props.delayNotification}
        onChange={props.handleDelayNotificationChange}
      />
      {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
      <label htmlFor="delay-notification" className="margin-left-sm margin-bottom-sm">
        Delay the notification
      </label>
    </div>
    {props.delayNotification
    && (
      <div className="col-md-12 margin-top-sm">
        {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
        <label htmlFor="notification-time">Notification date</label>
        <DatePicker
          id="notification-time"
          showTimeSelect
          value={props.notificationTime}
          onChange={props.handleNotificationTimeChange}
          minDate={new Date()}
        />
      </div>
    )}
    <form
      className="col-md-12"
      onSubmit={props.sendAssignedModules}
    >
      <button
        type="submit"
        className="btn btn-primary btn-block margin-x-md padding-x-sm"
        disabled={!props.validate() || props.assignInProgress}
      >
        <h4>Assign!</h4>
      </button>
      <div style={{ marginBottom: '200px' }}>
        <ClimbingBoxLoader
          color="#000000"
          loading={props.assignInProgress}
        />
      </div>
    </form>
  </div>
);

export default GroupAssignFrom;

GroupAssignFrom.propTypes = {
  resetLogoutCounter: PropTypes.func.isRequired,
  handleModuleChange: PropTypes.func.isRequired,
  handleDelayNotificationChange: PropTypes.func.isRequired,
  handleNotificationTimeChange: PropTypes.func.isRequired,
  sendAssignedModules: PropTypes.func.isRequired,
  validate: PropTypes.func.isRequired,
  selectedModules: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.string),
    PropTypes.shape({}),
  ]),
  delayNotification: PropTypes.bool,
  notificationTime: PropTypes.string,
  disableModuleSelect: PropTypes.bool,
  assignInProgress: PropTypes.bool,
  modulesOptions: PropTypes.arrayOf(PropTypes.shape({})),
};

GroupAssignFrom.defaultProps = {
  selectedModules: null,
  delayNotification: false,
  notificationTime: null,
  disableModuleSelect: false,
  assignInProgress: false,
  modulesOptions: [],
};
