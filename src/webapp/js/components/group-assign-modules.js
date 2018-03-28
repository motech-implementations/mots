import React, { Component } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import { Async } from 'react-select';
import PropTypes from 'prop-types';
import Alert from 'react-s-alert';
import DateTime from 'react-datetime';

import 'react-datetime/css/react-datetime.css';

import { resetLogoutCounter } from '../actions/index';
import apiClient from '../utils/api-client';
import { ASSIGN_MODULES_AUTHORITY, hasAuthority } from '../utils/authorization';

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

  static fetchDistricts() {
    const url = '/api/districtsOnly';

    return apiClient.get(url)
      .then((response) => {
        const districtList = _.map(
          response.data,
          district => ({ value: district.id, label: district.name }),
        );
        return { options: districtList };
      });
  }

  constructor(props) {
    super(props);
    this.state = {
      selectedModules: '',
      selectedDistrict: '',
      startDate: '',
      endDate: '',
    };

    this.handleModuleChange = this.handleModuleChange.bind(this);
    this.handleDistrictChange = this.handleDistrictChange.bind(this);
    this.handleStartDateChange = this.handleStartDateChange.bind(this);
    this.handleEndDateChange = this.handleEndDateChange.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.validate = this.validate.bind(this);
    this.validateDates = this.validateDates.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(ASSIGN_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    }
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

      const callback = () => {
        this.props.history.push('/chw/selected');
        Alert.success('Modules have been assigned!');
      };

      apiClient.post(url, payload)
        .then(() => callback());
    }
  }

  handleDistrictChange = (selectedDistrict) => {
    this.setState({ selectedDistrict });
  };

  handleModuleChange(selectedModules) {
    this.setState({ selectedModules });
  }

  handleStartDateChange(startDate) {
    const dateFormat = 'YYYY-MM-DD';
    const formattedDate = !startDate || typeof startDate === 'string' ? startDate : startDate.format(dateFormat);
    this.setState({ startDate: formattedDate });
  }

  handleEndDateChange(endDate) {
    const dateFormat = 'YYYY-MM-DD';
    const formattedDate = !endDate || typeof endDate === 'string' ? endDate : endDate.format(dateFormat);
    this.setState({ endDate: formattedDate });
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
    const empty = this.state.selectedDistrict === '' || this.state.selectedModules === ''
      || this.state.selectedModules.length === 0 || this.state.startDate === '' || this.state.endDate === '';
    return !nullable && !empty;
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Assign Modules to a District</h1>
        <div className="col-md-8 col-md-offset-2">

          <Async
            name="form-field-name"
            value={this.state.selectedDistrict}
            loadOptions={DistrictAssignModules.fetchDistricts}
            onChange={this.handleDistrictChange}
            onFocus={() => this.props.resetLogoutCounter()}
            placeholder="Select a District"
            className="margin-bottom-sm col-md-12"
            menuContainerStyle={{ zIndex: 5 }}
          />
          <Async
            value={this.state.selectedModules}
            loadOptions={DistrictAssignModules.fetchAvailableModules}
            onChange={this.handleModuleChange}
            onFocus={() => this.props.resetLogoutCounter()}
            disabled={this.state.selectedDistrict === ''}
            placeholder="Select Modules assignment"
            multi
            className="margin-bottom-sm col-md-12"
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
                inputProps={{ disabled: this.state.selectedModules === '' }}
                value={this.state.startDate}
                onChange={this.handleStartDateChange}
                onFocus={() => this.props.resetLogoutCounter()}
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
                inputProps={{ disabled: this.state.selectedModules === '' }}
                value={this.state.endDate}
                onChange={this.handleEndDateChange}
                onFocus={() => this.props.resetLogoutCounter()}
                id="end-date"
              />
            </div>
          </div>
          <form
            className="form-horizontal"
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

export default connect(null, { resetLogoutCounter })(DistrictAssignModules);

DistrictAssignModules.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
