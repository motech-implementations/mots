import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import Select from 'react-select';
import PropTypes from 'prop-types';
import Alert from 'react-s-alert';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

import 'react-datetime/css/react-datetime.css';

import { resetLogoutCounter, fetchLocations } from '../actions/index';
import apiClient from '../utils/api-client';
import { ASSIGN_MODULES_AUTHORITY, hasAuthority } from '../utils/authorization';
import { getSelectableLocations } from '../utils/form-utils';
import GroupAssignFrom from './group-assign-form';

class DistrictAssignModules extends Component {
  constructor(props) {
    super(props);
    this.state = {
      chiefdomOptions: [],
      facilityOptions: [],
      selectedModules: null,
      selectedDistrict: null,
      selectedChiefdom: null,
      selectedFacility: null,
      startDate: '',
      endDate: '',
      delayNotification: false,
      notificationTime: '',
      groups: [],
      selectedGroup: null,
      selectedIndex: 0,
    };

    this.handleModuleChange = this.handleModuleChange.bind(this);
    this.handleDistrictChange = this.handleDistrictChange.bind(this);
    this.handleChiefdomChange = this.handleChiefdomChange.bind(this);
    this.handleStartDateChange = this.handleStartDateChange.bind(this);
    this.handleEndDateChange = this.handleEndDateChange.bind(this);
    this.handleDelayNotificationChange = this.handleDelayNotificationChange.bind(this);
    this.handleNotificationTimeChange = this.handleNotificationTimeChange.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.validateGroup = this.validateGroup.bind(this);
    this.validateLocation = this.validateLocation.bind(this);
    this.handleGroupChange = this.handleGroupChange.bind(this);
    this.handleTabSelect = this.handleTabSelect.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(ASSIGN_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.props.fetchLocations();
    this.fetchGroups();
  }

  getChiefdomOptions(selectedDistrict) {
    if (selectedDistrict != null) {
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
    return [];
  }

  getFacilityOptions(selectedChiefdom) {
    if (selectedChiefdom != null) {
      const facilities = getSelectableLocations(
        'facilities',
        this.props.availableLocations,
        this.state.selectedDistrict.value,
        selectedChiefdom.value,
      );
      return _.map(
        facilities,
        facility => ({ value: facility.id, label: facility.name }),
      );
    }
    return [];
  }

  fetchGroups() {
    apiClient.get('/api/group')
      .then((response) => {
        if (response) {
          const groups = _.map(response.data, group => ({ value: group.id, label: group.name }));
          this.setState({ groups });
        }
      });
  }

  sendAssignedModules() {
    if (this.validateDates()) {
      const url = `/api/module/${this.state.selectedIndex === 0 ? 'district' : 'group'}/assign`;

      const payload = {
        modules: _.map(this.state.selectedModules, module => module.value),
        startDate: this.state.startDate,
        endDate: this.state.endDate,
      };

      if (this.state.selectedIndex === 0) {
        payload.districtId = this.state.selectedDistrict.value;

        if (this.state.selectedChiefdom !== null) {
          payload.chiefdomId = this.state.selectedChiefdom.value;
        }
        if (this.state.selectedFacility !== null) {
          payload.facilityId = this.state.selectedFacility.value;
        }
      } else {
        payload.groupId = this.state.selectedGroup.value;
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
      selectedFacility: null,
      chiefdomOptions: this.getChiefdomOptions(selectedDistrict),
    });
  };

  handleChiefdomChange = (selectedChiefdom) => {
    this.setState({
      selectedChiefdom,
      selectedFacility: null,
      facilityOptions: this.getFacilityOptions(selectedChiefdom),
    });
  };

  handleFacilityChange = (selectedFacility) => {
    this.setState({ selectedFacility });
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

  handleDelayNotificationChange(event) {
    this.setState({ delayNotification: event.target.checked });
  }

  handleNotificationTimeChange(notificationTime) {
    const dateFormat = 'YYYY-MM-DD HH:mm';
    const formattedTime = (notificationTime)
      ? notificationTime.clone().utc().format(dateFormat) : notificationTime;
    this.setState({ notificationTime: formattedTime });
    this.props.resetLogoutCounter();
  }

  handleGroupChange(selectedGroup) {
    this.setState({ selectedGroup });
  }

  handleTabSelect(index) {
    this.setState({ selectedIndex: index });
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
    const empty = !this.state.selectedModules
      || this.state.selectedModules.length === 0 || !this.state.startDate || !this.state.endDate
      || (this.state.delayNotification && this.state.notificationTime.length === 0);
    return !empty;
  }

  validateGroup() {
    return this.state.selectedGroup && this.validate();
  }

  validateLocation() {
    return this.state.selectedDistrict && this.validate();
  }

  render() {
    return (
      <div className="form-horizontal">
        <h1 className="page-header padding-bottom-xs margin-x-sm text-center">Assign Modules</h1>
        <Tabs selectedIndex={this.state.selectedIndex} onSelect={this.handleTabSelect}>
          <TabList>
            <Tab>Location assignment</Tab>
            <Tab>Group assignment</Tab>
          </TabList>

          <TabPanel>
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
              <Select
                name="facility"
                value={this.state.selectedFacility}
                options={this.state.facilityOptions}
                disabled={!this.state.selectedChiefdom}
                onChange={this.handleFacilityChange}
                onFocus={() => this.props.resetLogoutCounter()}
                placeholder="Select a Facility (optional)"
                className="margin-bottom-md col-md-12"
                menuContainerStyle={{ zIndex: 5 }}
              />

              <GroupAssignFrom
                resetLogoutCounter={() => this.props.resetLogoutCounter()}
                sendAssignedModules={this.sendAssignedModules}
                validate={this.validateLocation}
                handleModuleChange={this.handleModuleChange}
                handleStartDateChange={this.handleStartDateChange}
                handleEndDateChange={this.handleEndDateChange}
                handleDelayNotificationChange={this.handleDelayNotificationChange}
                handleNotificationTimeChange={this.handleNotificationTimeChange}
                selectedModules={this.state.selectedModules}
                startDate={this.state.startDate}
                endDate={this.state.endDate}
                delayNotification={this.state.delayNotification}
                disableModuleSelect={!this.state.selectedDistrict}
              />
            </div>
          </TabPanel>
          <TabPanel>
            <div className="col-md-8 col-md-offset-2">

              <Select
                name="group"
                value={this.state.selectedGroup}
                options={this.state.groups}
                onChange={this.handleGroupChange}
                onFocus={() => this.props.resetLogoutCounter()}
                placeholder="Select a Group"
                className="margin-bottom-md col-md-12"
                menuContainerStyle={{ zIndex: 5 }}
              />

              <GroupAssignFrom
                resetLogoutCounter={() => this.props.resetLogoutCounter()}
                sendAssignedModules={this.sendAssignedModules}
                validate={this.validateGroup}
                handleModuleChange={this.handleModuleChange}
                handleStartDateChange={this.handleStartDateChange}
                handleEndDateChange={this.handleEndDateChange}
                handleDelayNotificationChange={this.handleDelayNotificationChange}
                handleNotificationTimeChange={this.handleNotificationTimeChange}
                selectedModules={this.state.selectedModules}
                startDate={this.state.startDate}
                endDate={this.state.endDate}
                delayNotification={this.state.delayNotification}
                disableModuleSelect={!this.state.selectedGroup}
              />
            </div>
          </TabPanel>
        </Tabs>
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
