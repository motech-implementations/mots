import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import DualListBox from 'react-dual-listbox';
import Alert from 'react-s-alert';
import 'react-dual-listbox/lib/react-dual-listbox.css';

import apiClient from '../utils/api-client';

export default class AssignModules extends Component {
  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      selectedModules: [],
      chwData: {
        firstName: '',
        secondName: '',
        otherName: '',
      },
    };

    this.onChange = this.onChange.bind(this);
    this.fetchAvailableModules = this.fetchAvailableModules.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.fetchChwModules = this.fetchChwModules.bind(this);
  }

  componentWillMount() {
    this.fetchAvailableModules();
  }

  onChange(selectedModules) {
    this.setState({ selectedModules });
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
        this.fetchChwModules();
      });
  }

  fetchChwModules() {
    const url = '/api/assignedModules';
    const params = {
      chwId: this.props.match.params.chwId,
    };

    apiClient.get(url, { params })
      .then((response) => {
        const selectedModules = response.data.modules;

        let chwNamesData = _.pick(response.data.chw, ['firstName', 'secondName',
          'otherName']);
        chwNamesData = _.mapValues(
          chwNamesData,
          name => (_.isNil(name) ? ' ' : name),
        );

        this.setState({ selectedModules });
        this.setState({ chwData: chwNamesData });
      });
  }

  sendAssignedModules() {
    const url = '/api/assignModules';

    const payload = {
      modules: this.state.selectedModules,
      chwId: this.props.match.params.chwId,
    };

    const callback = () => {
      Alert.success('Modules have been assigned!');
    };

    apiClient.post(url, payload)
      .then(() => callback());
  }

  render() {
    const { selectedModules } = this.state;
    const { availableModulesList } = this.state;
    const { chwData } = this.state;

    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Assign Modules</h1>
        <h4>CHW: {`${chwData.firstName} ${chwData.otherName} ${chwData.secondName}`}</h4>
        <DualListBox
          canFilter
          options={availableModulesList}
          selected={selectedModules}
          filterPlaceholder="Filter..."
          onChange={this.onChange}
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
