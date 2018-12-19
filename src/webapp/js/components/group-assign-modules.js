import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import Select, { Async } from 'react-select';
import PropTypes from 'prop-types';
import Alert from 'react-s-alert';
import DateTime from 'react-datetime';

import 'react-datetime/css/react-datetime.css';

import { resetLogoutCounter, fetchLocations } from '../actions/index';
import apiClient from '../utils/api-client';
import { ASSIGN_MODULES_AUTHORITY, hasAuthority } from '../utils/authorization';
import { getSelectableLocations } from '../utils/form-utils';

class DistrictAssignModules extends Component {
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

  constructor(props) {
    super(props);
    this.state = {
      chiefdomOptions: [],
      selectedModules: '',
      selectedDistrict: '',
      selectedChiefdom: '',
      startDate: '',
      endDate: '',
      delayNotification: false,
      notificationTime: '',
    };

    this.handleModuleChange = this.handleModuleChange.bind(this);
    this.handleDistrictChange = this.handleDistrictChange.bind(this);
    this.handleChiefdomChange = this.handleChiefdomChange.bind(this);
    this.handleStartDateChange = this.handleStartDateChange.bind(this);
    this.handleEndDateChange = this.handleEndDateChange.bind(this);
    this.handleNotificationTimeChange = this.handleNotificationTimeChange.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.validate = this.validate.bind(this);
    this.validateDates = this.validateDates.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(ASSIGN_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.props.fetchLocations();
  }

  getChiefdomOptions(selectedDistrict) {
    const chiefdoms = getSelectableLocations(
      'chiefdoms',
      this.props.availableLocations,
      selectedDistrict.value,
    );
    return _.map(
      chiefdoms,
      chiefdom => ({ value: chiefdom.id, label: chiefdom.name }),
    );
  }

  sendAssignedModules() {
    if (this.validateDates()) {
      const url = '/api/module/district/assign';

      const payload = {
        modules: _.map(this.state.selectedModules, module => module.value),
        districtId: this.state.selectedDistrict.value,
        startDate: this.state.startDate,
        endDate: this.state.endDate,
      };
      if (this.state.selectedChiefdom !== null) {
        payload.chiefdomId = this.state.selectedChiefdom.value;
      }
      if (this.state.delayNotification && this.state.notificationTime) {
        payload.notificationTime = this.state.notificationTime;
      }
      const callback = () => {
        this.props.history.push('/chw/selected');
        Alert.success('Modules have been assigned!');
      };

      apiClient.post(url, payload)
        .then(() => callback());
    }
  }

  handleDistrictChange = (selectedDistrict) => {
    this.setState({
      selectedDistrict,
      selectedChiefdom: null,
      chiefdomOptions: this.getChiefdomOptions(selectedDistrict),
    });
  };

  handleChiefdomChange = (selectedChiefdom) => {
    this.setState({ selectedChiefdom });
  };

  handleModuleChange(selectedModules) {
    this.setState({ selectedModules });
  }

  handleStartDateChange(startDate) {
    const dateFormat = 'YYYY-MM-DD';
    const formattedDate = !startDate || typeof startDate === 'string' ? startDate : startDate.format(dateFormat);
    this.setState({ startDate: formattedDate });
    this.props.resetLogoutCounter();
  }

  handleEndDateChange(endDate) {
    const dateFormat = 'YYYY-MM-DD';
    const formattedDate = !endDate || typeof endDate === 'string' ? endDate : endDate.format(dateFormat);
    this.setState({ endDate: formattedDate });
    this.props.resetLogoutCounter();
  }

  handleNotificationTimeChange(notificationTime) {
    const dateFormat = 'YYYY-MM-DD HH:mm';
    const formattedTime = (notificationTime)
      ? notificationTime.utc().format(dateFormat) : notificationTime;
    this.setState({ notificationTime: formattedTime });
    this.props.resetLogoutCounter();
  }

  validateDates() {
    const start = new Date(this.state.startDate);
    const end = new Date(this.state.endDate);
    if (start > end) {
      Alert.error('End date must be after start date.');
      return false;
    }
    return true;
  }

  validate() {
    const nullable = !this.state.selectedDistrict || !this.state.selectedModules
      || !this.state.startDate || !this.state.endDate;
    const empty = !this.state.selectedDistrict || !this.state.selectedModules
      || this.state.selectedModules.length === 0 || !this.state.startDate || !this.state.endDate
      || (this.state.delayNotification && this.state.notificationTime.length === 0);
    return !nullable && !empty;
  }

  render() {
    return (
      <div className="form-horizontal">
        <h1 className="page-header padding-bottom-xs margin-x-sm text-center">Assign Modules to a location</h1>
        <div className="col-md-8 col-md-offset-2">

          <Select
            name="district"
            value={this.state.selectedDistrict}
            options={this.props.districtOptions}
            onChange={this.handleDistrictChange}
            onFocus={() => this.props.resetLogoutCounter()}
            placeholder="Select a District"
            className="margin-bottom-md col-md-12"
            menuContainerStyle={{ zIndex: 5 }}
          />
          <Select
            name="chiefdom"
            value={this.state.selectedChiefdom}
            options={this.state.chiefdomOptions}
            disabled={!this.state.selectedDistrict}
            onChange={this.handleChiefdomChange}
            onFocus={() => this.props.resetLogoutCounter()}
            placeholder="Select a Chiefdom (optional)"
            className="margin-bottom-md col-md-12"
            menuContainerStyle={{ zIndex: 5 }}
          />
          <Async
            value={this.state.selectedModules}
            loadOptions={DistrictAssignModules.fetchAvailableModules}
            onChange={this.handleModuleChange}
            onFocus={() => this.props.resetLogoutCounter()}
            disabled={!this.state.selectedDistrict}
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
                inputProps={{ disabled: !this.state.selectedModules }}
                value={this.state.startDate}
                onChange={this.handleStartDateChange}
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
                inputProps={{ disabled: !this.state.selectedModules }}
                value={this.state.endDate}
                onChange={this.handleEndDateChange}
                id="end-date"
              />
            </div>
          </div>
          <div className="col-md-12 margin-top-xs margin-bottom-xs">
            <input
              id="delay-notification"
              type="checkbox"
              className="checkbox-inline"
              checked={this.state.delayNotification}
              onChange={event => this.setState({ delayNotification: event.target.checked })}
            />
            <label htmlFor="delay-notification" className="margin-left-sm margin-bottom-sm">
              Delay the notification
            </label>
          </div>
          {this.state.delayNotification &&
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
                onChange={this.handleNotificationTimeChange}
                id="notification-time"
              />
            </div>
          </div>
          }
          <form
            className="form-horizontal col-md-12"
            onSubmit={this.sendAssignedModules}
          >
            <button
              type="submit"
              className="btn btn-primary btn-block margin-x-md padding-x-sm"
              disabled={!this.validate()}
            >
              <h4>Assign!</h4>
            </button>
          </form>
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  const districtOptions = _.map(
    state.availableLocations,
    district => ({ value: district.id, label: district.name }),
  );
  return {
    availableLocations: state.availableLocations,
    districtOptions,
  };
}

export default
connect(mapStateToProps, { fetchLocations, resetLogoutCounter })(DistrictAssignModules);

DistrictAssignModules.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtOptions: PropTypes.arrayOf(PropTypes.shape({})),
};

DistrictAssignModules.defaultProps = {
  availableLocations: [],
  districtOptions: [],
};
