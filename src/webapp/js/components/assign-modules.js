import _ from 'lodash';
import React, { Component } from 'react';
import DualListBox from 'react-dual-listbox';
import axios from 'axios';

import 'react-dual-listbox/lib/react-dual-listbox.css';

export default class AssignModules extends Component {

  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      selectedModules: [],
      chwData: {
        firstName: '',
        secondName: '',
        otherName: ''
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

  fetchChwModules() {

    const token = localStorage.getItem('token');
    const url = `/api/assignedModules`;
    const params = {
      access_token: token,
      chwId: this.props.match.params.chwId
    };

    axios.get(url, {params})
    .then((response) => {
      const selectedModules = response.data.modules;
      const chwData = response.data.chw;
      this.setState({selectedModules});
      this.setState({chwData});
    })
    .catch(function (error) {
      alert(error);
    });
  }

  fetchAvailableModules() {
    const token = localStorage.getItem('token');
    const url = `/api/modules`;
    const params = {
      access_token: token
    };

    axios.get(url, {params}).then((response) => {
      const availableModulesList = _.map(response.data,
          module => ({value: module.id, label: module.name}));
      this.setState({availableModulesList});
      this.fetchChwModules();
    });
  }

  sendAssignedModules() {
    const token = localStorage.getItem('token');
    const url = `/api/assignModules?access_token=${token}`;
    const params = {
      access_token: token
    };

    const payload = {
      modules: this.state.selectedModules,
      chwId: this.props.match.params.chwId
    };

    const callback = () => {
      this.props.history.push('/chw/');
      alert("Modules has been successfully assigned!")
    };

    axios.post(url, payload, {params}).then(() => callback());
  }

  onChange(selectedModules) {
    this.setState({ selectedModules });
  }

  render() {
    const { selectedModules } = this.state;
    const { availableModulesList } = this.state;
    const { chwData } = this.state;

    return (
        <div>
          <h1 className="page-header">Assign Modules</h1>
          <h3>CHW: {`${chwData.firstName} ${chwData.otherName} ${chwData.secondName}`}</h3>
          <DualListBox canFilter
                       options={availableModulesList}
                       selected={selectedModules}
                       filterPlaceholder="Filter..."
                       onChange={this.onChange}

          />
          <form className="form-horizontal"
                onSubmit={this.sendAssignedModules}>
            <button type="submit"
                    className="btn btn-primary btn-block
                    margin-x-md padding-x-sm">
              <h4>Assign!</h4>
            </button>
          </form>
        </div>
    );
  }
}