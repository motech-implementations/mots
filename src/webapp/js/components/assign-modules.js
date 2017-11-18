import _ from 'lodash';
import React, { Component } from 'react';
import DualListBox from 'react-dual-listbox';
import axios from 'axios';

import 'react-dual-listbox/lib/react-dual-listbox.css';

export default class AssignModules extends Component {

  constructor() {
    super();
    this.state = {
      selected: [],
      modulesList: [],
    };

    this.onChange = this.onChange.bind(this);
    this.fetchModules = this.fetchModules.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this)
  }

  componentWillMount() {
    this.fetchModules()
  }

  fetchModules() {
    const token = localStorage.getItem('token');
    const url = `/api/modules?access_token=${token}`;

    axios.get(url).then((response) => {
      const modulesList = _.map(response.data,
          module => ({value: module.id, label: module.name}));
      this.setState({modulesList});
    });
  }

  sendAssignedModules() {

    const token = localStorage.getItem('token');
    const url = `/api/assignModules?access_token=${token}`;

    const payload = {
      modules: this.state.selected,
      chwId: this.props.match.params.chwId
    };

    const callback = () => {
      this.props.history.push('/chw/');
      alert("Modules has been successfully assigned!")
    };

    axios.post(url,payload).then(() => callback());
  }

  onChange(selected) {
    this.setState({ selected });
  }

  render() {
    const { selected } = this.state;
    const { modulesList } = this.state;

    return (
        <div>
          <h1 className="page-header">Assign Modules</h1>
          <h3>CHW ID: {this.props.match.params.chwId}</h3>
          <DualListBox canFilter
                       options={modulesList}
                       selected={selected}
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