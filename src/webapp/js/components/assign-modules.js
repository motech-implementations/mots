import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import DualListBox from 'react-dual-listbox';
import Alert from 'react-s-alert';
import { Async } from 'react-select';

import 'react-select/dist/react-select.css';
import 'react-dual-listbox/lib/react-dual-listbox.css';

import apiClient from '../utils/api-client';

export default class AssignModules extends Component {
  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      selectedModules: [],
      selectedChw: this.props.match.params.chwId || '',
    };

    this.handleModuleChange = this.handleModuleChange.bind(this);
    this.fetchAvailableModules = this.fetchAvailableModules.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.fetchChwModules = this.fetchChwModules.bind(this);
  }

  componentWillMount() {
    this.fetchAvailableModules();
  }

  fetchAvailableModules() {
    const url = '/api/modules';

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
          chw => ({ value: chw.id, label: `${chw.chwId}: ${_.defaultTo(chw.firstName, '')} ${_.defaultTo(chw.otherName, '')} ${_.defaultTo(chw.secondName, '')}` }),
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
        const selectedModules = response.data.modules;
        this.setState({ selectedModules });
      });
  }

  sendAssignedModules() {
    const url = '/api/assignModules';

    const payload = {
      modules: this.state.selectedModules,
      chwId: this.state.selectedChw,
    };

    const callback = () => {
      Alert.success('Modules have been assigned!');
    };

    apiClient.post(url, payload)
      .then(() => callback());
  }

  handleChwChange = (selectedChw) => {
    this.setState({ selectedChw: selectedChw.value }, () => this.fetchChwModules());
  };

  handleModuleChange(selectedModules) {
    this.setState({ selectedModules });
  }

  render() {
    const { selectedModules } = this.state;
    const { availableModulesList } = this.state;

    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Assign Modules</h1>
        <Async
          name="form-field-name"
          value={this.state.selectedChw}
          loadOptions={this.fetchChwsInfo}
          onChange={this.handleChwChange}
          placeholder="Select a Community Health Worker"
          className="margin-bottom-md"
        />
        <DualListBox
          canFilter
          options={availableModulesList}
          selected={selectedModules}
          filterPlaceholder="Filter..."
          onChange={this.handleModuleChange}
          disabled={this.state.selectedChw === ''}
        />
        <form
          className="form-horizontal"
          onSubmit={this.sendAssignedModules}
        >
          <button
            type="submit"
            className="btn btn-primary btn-block
                    margin-x-md padding-x-sm"
          >
            <h4>Assign!</h4>
          </button>
        </form>
      </div>
    );
  }
}

AssignModules.propTypes = {
  match: PropTypes.shape({
    params: PropTypes.shape({
      chwId: PropTypes.string,
    }),
  }).isRequired,
};
