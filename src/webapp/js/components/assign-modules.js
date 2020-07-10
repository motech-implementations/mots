import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import DualListBox from 'react-dual-listbox';
import Alert from 'react-s-alert';
import { Async } from 'react-select';
import DateTime from 'react-datetime';

import 'react-select/dist/react-select.css';
import 'react-dual-listbox/lib/react-dual-listbox.css';

import { resetLogoutCounter } from '../actions/index';
import apiClient from '../utils/api-client';
import { hasAuthority, ASSIGN_MODULES_AUTHORITY } from '../utils/authorization';
import { getDefaultNotificationDate } from '../utils/form-utils';

class AssignModules extends Component {
  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      selectedModules: [],
      selectedChw: this.props.match.params.chwId || '',
      currentModules: [],
      delayNotification: false,
      notificationTime: '',
    };

    this.handleModuleChange = this.handleModuleChange.bind(this);
    this.fetchAvailableModules = this.fetchAvailableModules.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.fetchChwModules = this.fetchChwModules.bind(this);
    this.areModulesEqual = this.areModulesEqual.bind(this);
    this.handleNotificationTimeChange = this.handleNotificationTimeChange.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(ASSIGN_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.fetchAvailableModules();
    }
  }

  fetchAvailableModules() {
    const url = '/api/modules/simple';

    apiClient.get(url)
      .then((response) => {
        const availableModulesList = _.map(
          response.data,
          module => ({ value: module.id, label: module.name }),
        );
        this.setState({ availableModulesList });
        if (this.state.selectedChw) {
          this.fetchChwModules();
        }
      });
  }

  fetchChwsInfo = () => {
    const url = 'api/chwInfo';

    return apiClient.get(url)
      .then((response) => {
        const chwList = _.map(
          response.data,
          chw => ({ value: chw.id, label: `${chw.chwId}: ${_.defaultTo(chw.firstName, '')} ${_.defaultTo(chw.familyName, '')}` }),
        );
        return { options: chwList };
      });
  };

  fetchChwModules() {
    const url = '/api/assignedModules';
    const params = {
      chwId: this.state.selectedChw,
    };

    apiClient.get(url, { params })
      .then((response) => {
        const selectedModules = _.map(
          response.data.modules,
          module => module.id,
        );
        this.setState({
          selectedModules,
          currentModules: selectedModules,
        });
      });
  }

  sendAssignedModules() {
    const url = '/api/module/assign';

    const payload = {
      modules: this.state.selectedModules,
      chwId: this.state.selectedChw,
    };
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

  handleChwChange = (selectedChw) => {
    if (selectedChw) {
      this.setState({ selectedChw: selectedChw.value }, () => this.fetchChwModules());
    } else {
      this.setState({ selectedChw: '' });
    }
  };

  handleModuleChange(selectedModules) {
    this.setState({ selectedModules });
  }

  handleNotificationTimeChange(notificationTime) {
    const dateFormat = 'YYYY-MM-DD HH:mm';
    const formattedTime = (notificationTime)
      ? notificationTime.clone().utc().format(dateFormat) : notificationTime;
    this.setState({ notificationTime: formattedTime });
    this.props.resetLogoutCounter();
  }

  areModulesEqual() {
    return _.isEqual(this.state.selectedModules.sort(), this.state.currentModules.sort());
  }

  render() {
    const { selectedModules } = this.state;
    const { availableModulesList } = this.state;

    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm text-center">Assign Modules</h1>
        <div className="col-md-8 col-md-offset-2">
          <Async
            name="form-field-name"
            value={this.state.selectedChw}
            loadOptions={this.fetchChwsInfo}
            onChange={this.handleChwChange}
            onFocus={() => this.props.resetLogoutCounter()}
            placeholder="Select a Community Health Worker"
            className="margin-bottom-md"
          />
          <DualListBox
            canFilter
            options={availableModulesList}
            selected={selectedModules}
            filterPlaceholder="Filter..."
            onChange={this.handleModuleChange}
            onFocus={() => this.props.resetLogoutCounter()}
            disabled={this.state.selectedChw === ''}
          />
          <div className="col-md-12 margin-top-sm margin-bottom-xs">
            <input
              id="delay-notification"
              type="checkbox"
              className="checkbox-inline"
              checked={this.state.delayNotification}
              onChange={event => this.setState({ delayNotification: event.target.checked })}
            />
            {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
            <label htmlFor="delay-notification" className="margin-left-sm margin-bottom-sm">
              Delay the notification
            </label>
          </div>
          {this.state.delayNotification
          && (
          <div className="col-md-12 margin-top-sm">
            {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
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
                defaultValue={getDefaultNotificationDate()}
                isValidDate={current => current.isSameOrAfter(new Date(), 'day')}
              />
            </div>
          </div>
          )}
          <form
            className="form-horizontal col-md-12"
            onSubmit={this.sendAssignedModules}
          >
            <button
              type="submit"
              className="btn btn-primary btn-block
                      margin-x-md padding-x-sm"
              style={{ marginBottom: '200px' }}
              disabled={this.areModulesEqual()}
            >
              <h4>Assign!</h4>
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default connect(null, { resetLogoutCounter })(AssignModules);

AssignModules.propTypes = {
  match: PropTypes.shape({
    params: PropTypes.shape({
      chwId: PropTypes.string,
    }),
  }).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
