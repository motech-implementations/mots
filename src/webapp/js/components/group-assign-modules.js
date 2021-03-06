import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import PropTypes from 'prop-types';
import { toast } from 'react-toastify';
import {
  Tab, Tabs, TabList, TabPanel,
} from 'react-tabs';

import Select from '../utils/select';
import { resetLogoutCounter, fetchLocations } from '../actions/index';
import apiClient from '../utils/api-client';
import { ASSIGN_MODULES_AUTHORITY, hasAuthority } from '../utils/authorization';
import { getSelectableLocations } from '../utils/form-utils';
import GroupAssignFrom from './group-assign-form';

class DistrictAssignModules extends Component {
  constructor(props) {
    super(props);
    this.state = {
      sectorOptions: [],
      facilityOptions: [],
      modulesOptions: [],
      selectedModules: null,
      selectedDistrict: null,
      selectedSector: null,
      selectedFacility: null,
      delayNotification: false,
      notificationTime: '',
      groups: [],
      selectedGroup: null,
      selectedIndex: 0,
      assignInProgress: false,
    };

    this.handleModuleChange = this.handleModuleChange.bind(this);
    this.handleDistrictChange = this.handleDistrictChange.bind(this);
    this.handleSectorChange = this.handleSectorChange.bind(this);
    this.handleDelayNotificationChange = this.handleDelayNotificationChange.bind(this);
    this.handleNotificationTimeChange = this.handleNotificationTimeChange.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.validateGroup = this.validateGroup.bind(this);
    this.validateLocation = this.validateLocation.bind(this);
    this.handleGroupChange = this.handleGroupChange.bind(this);
    this.handleTabSelect = this.handleTabSelect.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(ASSIGN_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.props.fetchLocations();
    this.fetchGroups();
    this.fetchModules();
  }

  getSectorOptions(selectedDistrict) {
    if (selectedDistrict != null) {
      const sectors = getSelectableLocations(
        'sectors',
        this.props.availableLocations,
        selectedDistrict,
      );
      return _.map(
        sectors,
        sector => ({ value: sector.id, label: sector.name }),
      );
    }
    return [];
  }

  getFacilityOptions(selectedSector) {
    if (selectedSector != null) {
      const facilities = getSelectableLocations(
        'facilities',
        this.props.availableLocations,
        this.state.selectedDistrict,
        selectedSector,
      );
      return _.map(
        facilities,
        facility => ({ value: facility.id, label: facility.name }),
      );
    }
    return [];
  }

  handleDistrictChange = (selectedDistrict) => {
    this.setState({
      selectedDistrict,
      selectedSector: null,
      selectedFacility: null,
      sectorOptions: this.getSectorOptions(selectedDistrict),
    });
  };

  handleSectorChange = (selectedSector) => {
    this.setState({
      selectedSector,
      selectedFacility: null,
      facilityOptions: this.getFacilityOptions(selectedSector),
    });
  };

  handleFacilityChange = (selectedFacility) => {
    this.setState({ selectedFacility });
  };

  handleModuleChange(selectedModules) {
    this.setState({ selectedModules });
  }

  handleDelayNotificationChange(event) {
    this.setState({ delayNotification: event.target.checked });
  }

  handleNotificationTimeChange(notificationTime) {
    this.setState({ notificationTime });
    this.props.resetLogoutCounter();
  }

  handleGroupChange(selectedGroup) {
    this.setState({ selectedGroup });
  }

  handleTabSelect(index) {
    this.setState({ selectedIndex: index });
    this.props.resetLogoutCounter();
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

  fetchModules() {
    apiClient.get('/api/modules/simple')
      .then((response) => {
        if (response) {
          const modulesOptions = _.map(response.data,
            module => ({ value: module.id, label: module.name }));
          this.setState({ modulesOptions });
        }
      });
  }

  sendAssignedModules() {
    const url = `/api/module/${this.state.selectedIndex === 0 ? 'district' : 'group'}/assign`;

    const payload = {
      modules: this.state.selectedModules,
    };

    this.setState({ assignInProgress: true });

    if (this.state.selectedIndex === 0) {
      payload.districtId = this.state.selectedDistrict;

      if (this.state.selectedSector !== null) {
        payload.sectorId = this.state.selectedSector;
      }
      if (this.state.selectedFacility !== null) {
        payload.facilityId = this.state.selectedFacility;
      }
    } else {
      payload.groupId = this.state.selectedGroup;
    }

    if (this.state.delayNotification && this.state.notificationTime) {
      payload.notificationTime = this.state.notificationTime;
    }

    const callback = (assigned) => {
      if (assigned) {
        this.props.history.push('/chw/selected');
        toast.success('Modules have been assigned!');
      } else {
        toast.success('Module was already assigned!');
        this.setState({ assignInProgress: false });
      }
    };

    apiClient.post(url, payload)
      .then(response => callback(response.data));
  }

  validate() {
    const empty = !this.state.selectedModules
      || this.state.selectedModules.length === 0
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
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm text-center">Assign Modules</h1>
        <Tabs selectedIndex={this.state.selectedIndex} onSelect={this.handleTabSelect}>
          <TabList>
            <Tab>Location assignment</Tab>
            <Tab>Group assignment</Tab>
          </TabList>

          <TabPanel>
            <div className="col-md-8 offset-md-2">
              <Select
                name="district"
                value={this.state.selectedDistrict}
                options={this.props.districtOptions}
                onChange={this.handleDistrictChange}
                onFocus={() => this.props.resetLogoutCounter()}
                placeholder="Select a District"
                className="margin-bottom-md col-md-12"
              />
              <Select
                name="sector"
                value={this.state.selectedSector}
                options={this.state.sectorOptions}
                isDisabled={!this.state.selectedDistrict}
                onChange={this.handleSectorChange}
                onFocus={() => this.props.resetLogoutCounter()}
                placeholder="Select a Sector (optional)"
                className="margin-bottom-md col-md-12"
              />
              <Select
                name="facility"
                value={this.state.selectedFacility}
                options={this.state.facilityOptions}
                isDisabled={!this.state.selectedSector}
                onChange={this.handleFacilityChange}
                onFocus={() => this.props.resetLogoutCounter()}
                placeholder="Select a Facility (optional)"
                className="margin-bottom-md col-md-12"
              />

              <GroupAssignFrom
                resetLogoutCounter={() => this.props.resetLogoutCounter()}
                sendAssignedModules={this.sendAssignedModules}
                validate={this.validateLocation}
                handleModuleChange={this.handleModuleChange}
                handleDelayNotificationChange={this.handleDelayNotificationChange}
                handleNotificationTimeChange={this.handleNotificationTimeChange}
                modulesOptions={this.state.modulesOptions}
                selectedModules={this.state.selectedModules}
                delayNotification={this.state.delayNotification}
                notificationTime={this.state.notificationTime}
                disableModuleSelect={!this.state.selectedDistrict}
                assignInProgress={this.state.assignInProgress}
              />
            </div>
          </TabPanel>
          <TabPanel>
            <div className="col-md-8 offset-md-2">

              <Select
                name="group"
                value={this.state.selectedGroup}
                options={this.state.groups}
                onChange={this.handleGroupChange}
                onFocus={() => this.props.resetLogoutCounter()}
                placeholder="Select a Group"
                className="margin-bottom-md col-md-12"
              />

              <GroupAssignFrom
                resetLogoutCounter={() => this.props.resetLogoutCounter()}
                sendAssignedModules={this.sendAssignedModules}
                validate={this.validateGroup}
                handleModuleChange={this.handleModuleChange}
                handleDelayNotificationChange={this.handleDelayNotificationChange}
                handleNotificationTimeChange={this.handleNotificationTimeChange}
                modulesOptions={this.state.modulesOptions}
                selectedModules={this.state.selectedModules}
                delayNotification={this.state.delayNotification}
                notificationTime={this.state.notificationTime}
                disableModuleSelect={!this.state.selectedGroup}
                assignInProgress={this.state.assignInProgress}
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
